from abc import ABCMeta, abstractmethod
import os
import glob
import subprocess
import shutil
import multiprocessing
import re
import bb

DB_CONFIG_CONTENT = '''# ================ Environment
set_data_dir .
set_create_dir .
set_lg_dir ./log
set_tmp_dir ./tmp
set_flags db_log_autoremove on

# -- thread_count must be >= 8
set_thread_count 64

# ================ Logging

# ================ Memory Pool
set_cachesize 0 1048576 0
set_mp_mmapsize 268435456

# ================ Locking
set_lk_max_locks 16384
set_lk_max_lockers 16384
set_lk_max_objects 16384
mutex_set_max 163840

# ================ Replication
'''

SCRIPTLET_FORMAT = '''#!/bin/bash

export PATH=%s
export D=%s
export OFFLINE_ROOT="$D"
export IPKG_OFFLINE_ROOT="$D"
export OPKG_OFFLINE_ROOT="$D"
export INTERCEPT_DIR=%s
export NATIVE_ROOT=%s

$2 $1/$3 $4
if [ $? -ne 0 ]; then
  if [ $4 -eq 1 ]; then
    mkdir -p $1/etc/rpm-postinsts
    num=100
    while [ -e $1/etc/rpm-postinsts/${num}-* ]; do num=$((num + 1)); done
    name=`head -1 $1/$3 | cut -d\' \' -f 2`
    echo "#!$2" > $1/etc/rpm-postinsts/${num}-${name}
    echo "# Arg: $4" >> $1/etc/rpm-postinsts/${num}-${name}
    cat $1/$3 >> $1/etc/rpm-postinsts/${num}-${name}
    chmod +x $1/etc/rpm-postinsts/${num}-${name}
  else
    echo "Error: pre/post remove scriptlet failed"
  fi
fi
'''


# this can be used by all PM backends to create the index files in parallel
def create_index(arg):
    index_cmd = arg

    try:
        bb.note("Executing '%s' ..." % index_cmd)
        subprocess.check_output(index_cmd, shell=True)
    except subprocess.CalledProcessError as e:
        return("Index creation command %s failed with return code %d!" %
               (e.cmd, e.returncode))

    return None


class PackageManager(object):
    """
    This is an abstract class. Do not instantiate this directly.
    """
    __metaclass__ = ABCMeta

    def __init__(self, d):
        self.d = d
        self.deploy_dir = None
        self.deploy_lock = None

    """
    Update the package manager package database.
    """
    @abstractmethod
    def update(self):
        pass

    """
    Install a list of packages. 'pkgs' is a list object. If 'attempt_only' is
    True, installation failures are ignored.
    """
    @abstractmethod
    def install(self, pkgs, attempt_only=False):
        pass

    """
    Remove a list of packages. 'pkgs' is a list object. If 'with_dependencies'
    is False, the any dependencies are left in place.
    """
    @abstractmethod
    def remove(self, pkgs, with_dependencies=True):
        pass

    """
    This function creates the index files
    """
    @abstractmethod
    def write_index(self):
        pass

    @abstractmethod
    def remove_packaging_data(self):
        pass

    @abstractmethod
    def list_installed(self, format=None):
        pass

    """
    Install complementary packages based upon the list of currently installed
    packages e.g. locales, *-dev, *-dbg, etc. This will only attempt to install
    these packages, if they don't exist then no error will occur.  Note: every
    backend needs to call this function explicitly after the normal package
    installation
    """
    def install_complementary(self, globs=None):
        # we need to write the list of installed packages to a file because the
        # oe-pkgdata-util reads it from a file
        installed_pkgs_file = os.path.join(self.d.getVar('WORKDIR', True),
                                           "installed_pkgs.txt")
        with open(installed_pkgs_file, "w+") as installed_pkgs:
            installed_pkgs.write(self.list_installed("arch"))

        if globs is None:
            globs = self.d.getVar('IMAGE_INSTALL_COMPLEMENTARY', True)
            split_linguas = set()

            for translation in self.d.getVar('IMAGE_LINGUAS', True).split():
                split_linguas.add(translation)
                split_linguas.add(translation.split('-')[0])

            split_linguas = sorted(split_linguas)

            for lang in split_linguas:
                globs += " *-locale-%s" % lang

        if globs is None:
            return

        cmd = [bb.utils.which(os.getenv('PATH'), "oe-pkgdata-util"),
               "glob", self.d.getVar('PKGDATA_DIR', True), installed_pkgs_file,
               globs]
        try:
            bb.note("Installing complementary packages ...")
            complementary_pkgs = subprocess.check_output(cmd)
        except subprocess.CalledProcessError as e:
            bb.fatal("Could not compute complementary packages list. Command "
                     "%s returned %d!" % (' '.join(cmd), e.returncode))

        self.install(complementary_pkgs.split(), attempt_only=True)

    def deploy_dir_lock(self):
        if self.deploy_dir is None:
            raise RuntimeError("deploy_dir is not set!")

        lock_file_name = os.path.join(self.deploy_dir, "deploy.lock")

        self.deploy_lock = bb.utils.lockfile(lock_file_name)

    def deploy_dir_unlock(self):
        if self.deploy_lock is None:
            return

        bb.utils.unlockfile(self.deploy_lock)

        self.deploy_lock = None


class RpmPM(PackageManager):
    def __init__(self,
                 d,
                 target_rootfs,
                 package_archs,
                 target_os,
                 target_vendor,
                 task_name='target',
                 providename=None):
        super(RpmPM, self).__init__(d)
        self.target_rootfs = target_rootfs
        self.ml_os_list = target_os
        self.target_vendor = target_vendor
        self.task_name = task_name
        self.providename = providename
        self.fullpkglist = list()
        self.deploy_dir = self.d.getVar('DEPLOY_DIR_RPM', True)
        self.etcrpm_dir = os.path.join(self.target_rootfs, "etc/rpm")
        self.install_dir = os.path.join(self.target_rootfs, "install")
        self.rpm_cmd = bb.utils.which(os.getenv('PATH'), "rpm")
        self.smart_cmd = bb.utils.which(os.getenv('PATH'), "smart")
        self.smart_opt = "--data-dir=" + os.path.join(target_rootfs,
                                                      'var/lib/smart')
        self.scriptlet_wrapper = self.d.expand('${WORKDIR}/scriptlet_wrapper')
        self.solution_manifest = self.d.expand('${T}/saved/%s_solution' %
                                               self.task_name)
        self.saved_rpmlib = self.d.expand('${T}/saved/%s' % self.task_name)
        self.image_rpmlib = os.path.join(self.target_rootfs, 'var/lib/rpm')

        if not os.path.exists(self.d.expand('${T}/saved')):
            bb.utils.mkdirhier(self.d.expand('${T}/saved'))

        # arch order is reversed.  This ensures the -best- match is
        # listed first!
        self.ml_prefix_list = dict()
        for mlib in package_archs:
            if mlib == 'default':
                self.ml_prefix_list[mlib] = package_archs[mlib]
            else:
                self.ml_prefix_list[mlib] = list()
                for arch in package_archs[mlib]:
                    if arch in ['all', 'noarch', 'any']:
                        self.ml_prefix_list[mlib].append(arch)
                    else:
                        self.ml_prefix_list[mlib].append(mlib + "_" + arch)

    '''
    Create configs for rpm and smart, and multilib is supported
    '''
    def create_configs(self):
        target_arch = self.d.getVar('TARGET_ARCH', True)
        platform = '%s%s-%s' % (target_arch.replace('-', '_'),
                                self.target_vendor,
                                self.ml_os_list['default'])

        # List must be prefered to least preferred order
        default_platform_extra = list()
        platform_extra = list()
        bbextendvariant = self.d.getVar('BBEXTENDVARIANT', True) or ""
        for mlib in self.ml_os_list:
            for arch in self.ml_prefix_list[mlib]:
                plt = arch.replace('-', '_') + '-.*-' + self.ml_os_list[mlib]
                if mlib == bbextendvariant:
                    if plt not in default_platform_extra:
                        default_platform_extra.append(plt)
                else:
                    if plt not in platform_extra:
                        platform_extra.append(plt)
        platform_extra = default_platform_extra + platform_extra

        self._create_configs(platform, platform_extra)

    def _invoke_smart(self, args):
        cmd = "%s %s %s" % (self.smart_cmd, self.smart_opt, args)
        # bb.note(cmd)
        try:
            complementary_pkgs = subprocess.check_output(cmd, shell=True)
            # bb.note(complementary_pkgs)
            return complementary_pkgs
        except subprocess.CalledProcessError as e:
            bb.fatal("Could not invoke smart. Command "
                     "%s returned %d!" % (cmd, e.returncode))

    '''
    Translate the RPM/Smart format names to the OE multilib format names
    '''
    def _pkg_translate_smart_to_oe(self, pkg, arch):
        new_pkg = pkg
        fixed_arch = arch.replace('_', '-')
        found = 0
        for mlib in self.ml_prefix_list:
            for cmp_arch in self.ml_prefix_list[mlib]:
                fixed_cmp_arch = cmp_arch.replace('_', '-')
                if fixed_arch == fixed_cmp_arch:
                    if mlib == 'default':
                        new_pkg = pkg
                        new_arch = cmp_arch
                    else:
                        new_pkg = mlib + '-' + pkg
                        # We need to strip off the ${mlib}_ prefix on the arch
                        new_arch = cmp_arch.replace(mlib + '_', '')

                    # Workaround for bug 3565. Simply look to see if we
                    # know of a package with that name, if not try again!
                    filename = os.path.join(self.d.getVar('PKGDATA_DIR', True),
                                            'runtime-reverse',
                                            new_pkg)
                    if os.path.exists(filename):
                        found = 1
                        break

            if found == 1 and fixed_arch == fixed_cmp_arch:
                break
        #bb.note('%s, %s -> %s, %s' % (pkg, arch, new_pkg, new_arch))
        return new_pkg, new_arch

    def _search_pkg_name_in_feeds(self, pkg, feed_archs):
        for arch in feed_archs:
            arch = arch.replace('-', '_')
            for p in self.fullpkglist:
                if pkg in p and '@' + arch in p:
                    # First found is best match
                    # bb.note('%s -> %s' % (pkg, pkg + '@' + arch))
                    return pkg + '@' + arch

        return ""

    '''
    Translate the OE multilib format names to the RPM/Smart format names
    It searched the RPM/Smart format names in probable multilib feeds first,
    and then searched the default base feed.
    '''
    def _pkg_translate_oe_to_smart(self, pkgs, attempt_only=False):
        new_pkgs = list()

        for pkg in pkgs:
            new_pkg = pkg
            # Search new_pkg in probable multilibs first
            for mlib in self.ml_prefix_list:
                # Jump the default archs
                if mlib == 'default':
                    continue

                subst = pkg.replace(mlib + '-', '')
                # if the pkg in this multilib feed
                if subst != pkg:
                    feed_archs = self.ml_prefix_list[mlib]
                    new_pkg = self._search_pkg_name_in_feeds(subst, feed_archs)
                    if not new_pkg:
                        # Failed to translate, package not found!
                        err_msg = '%s not found in the %s feeds (%s).\n' % \
                                  (pkg, mlib, " ".join(feed_archs))
                        if not attempt_only:
                            err_msg += " ".join(self.fullpkglist)
                            bb.fatal(err_msg)
                        bb.warn(err_msg)
                    else:
                        new_pkgs.append(new_pkg)

                    break

            # Apparently not a multilib package...
            if pkg == new_pkg:
                # Search new_pkg in default archs
                default_archs = self.ml_prefix_list['default']
                new_pkg = self._search_pkg_name_in_feeds(pkg, default_archs)
                if not new_pkg:
                    err_msg = '%s not found in the base feeds (%s).\n' % \
                              (pkg, ' '.join(default_archs))
                    if not attempt_only:
                        err_msg += " ".join(self.fullpkglist)
                        bb.fatal(err_msg)
                    bb.warn(err_msg)
                else:
                    new_pkgs.append(new_pkg)

        return new_pkgs

    def _create_configs(self, platform, platform_extra):
        # Setup base system configuration
        bb.note("configuring RPM platform settings")

        # Configure internal RPM environment when using Smart
        os.environ['RPM_ETCRPM'] = self.etcrpm_dir
        bb.utils.mkdirhier(self.etcrpm_dir)

        # Setup temporary directory -- install...
        if os.path.exists(self.install_dir):
            bb.utils.remove(self.install_dir, True)
        bb.utils.mkdirhier(os.path.join(self.install_dir, 'tmp'))

        channel_priority = 5
        platform_dir = os.path.join(self.etcrpm_dir, "platform")
        with open(platform_dir, "w+") as platform_fd:
            platform_fd.write(platform + '\n')
            for pt in platform_extra:
                channel_priority += 5
                platform_fd.write(pt + '.*\n')

        # Tell RPM that the "/" directory exist and is available
        bb.note("configuring RPM system provides")
        sysinfo_dir = os.path.join(self.etcrpm_dir, "sysinfo")
        bb.utils.mkdirhier(sysinfo_dir)
        with open(os.path.join(sysinfo_dir, "Dirnames"), "w+") as dirnames:
            dirnames.write("/\n")

        if self.providename:
            providename_dir = os.path.join(sysinfo_dir, "Providename")
            if not os.path.exists(providename_dir):
                providename_content = '\n'.join(self.providename)
                providename_content += '\n'
                open(providename_dir, "w+").write(providename_content)

        # Configure RPM... we enforce these settings!
        bb.note("configuring RPM DB settings")
        # After change the __db.* cache size, log file will not be
        # generated automatically, that will raise some warnings,
        # so touch a bare log for rpm write into it.
        rpmlib_log = os.path.join(self.image_rpmlib, 'log', 'log.0000000001')
        if not os.path.exists(rpmlib_log):
            bb.utils.mkdirhier(os.path.join(self.image_rpmlib, 'log'))
            open(rpmlib_log, 'w+').close()
        db_config_dir = os.path.join(self.image_rpmlib, 'DB_CONFIG')
        if not os.path.exists(db_config_dir):
            open(db_config_dir, 'w+').write(DB_CONFIG_CONTENT)

        # Create database so that smart doesn't complain (lazy init)
        cmd = "%s --root %s --dbpath /var/lib/rpm -qa > /dev/null" % (
              self.rpm_cmd,
              self.target_rootfs)
        try:
            subprocess.check_output(cmd, shell=True)
        except subprocess.CalledProcessError as e:
            bb.fatal("Create rpm database failed. Command %s "
                     "returned %d" % (cmd, e.returncode))

        # Configure smart
        bb.note("configuring Smart settings")
        bb.utils.remove(os.path.join(self.target_rootfs, 'var/lib/smart'),
                        True)
        self._invoke_smart('config --set rpm-root=%s' % self.target_rootfs)
        self._invoke_smart('config --set rpm-dbpath=/var/lib/rpm')
        self._invoke_smart('config --set rpm-extra-macros._var=%s' %
                           self.d.getVar('localstatedir', True))
        cmd = 'config --set rpm-extra-macros._tmppath=/install/tmp'
        self._invoke_smart(cmd)

        # Write common configuration for host and target usage
        self._invoke_smart('config --set rpm-nolinktos=1')
        self._invoke_smart('config --set rpm-noparentdirs=1')
        for i in self.d.getVar('BAD_RECOMMENDATIONS', True).split():
            self._invoke_smart('flag --set ignore-recommends %s' % i)

        # Do the following configurations here, to avoid them being
        # saved for field upgrade
        if self.d.getVar('NO_RECOMMENDATIONS', True).strip() == "1":
            self._invoke_smart('config --set ignore-all-recommends=1')
        pkg_exclude = self.d.getVar('PACKAGE_EXCLUDE', True) or ""
        for i in pkg_exclude.split():
            self._invoke_smart('flag --set exclude-packages %s' % i)

        # Optional debugging
        # self._invoke_smart('config --set rpm-log-level=debug')
        # cmd = 'config --set rpm-log-file=/tmp/smart-debug-logfile'
        # self._invoke_smart(cmd)

        for canonical_arch in platform_extra:
            arch = canonical_arch.split('-')[0]
            arch_channel = os.path.join(self.deploy_dir, arch)
            if os.path.exists(arch_channel):
                bb.note('Note: adding Smart channel %s (%s)' %
                        (arch, channel_priority))
                self._invoke_smart('channel --add %s type=rpm-md baseurl=%s -y'
                                   % (arch, arch_channel))
                self._invoke_smart('channel --set %s priority=%d' %
                                   (arch, channel_priority))
                channel_priority -= 5

        bb.note('adding Smart RPM DB channel')
        self._invoke_smart('channel --add rpmsys type=rpm-sys -y')

        # Construct install scriptlet wrapper.
        # Scripts need to be ordered when executed, this ensures numeric order.
        # If we ever run into needing more the 899 scripts, we'll have to.
        # change num to start with 1000.
        #
        intercept_dir = self.d.expand('${WORKDIR}/intercept_scripts')
        native_root = self.d.getVar('STAGING_DIR_NATIVE', True)
        scriptlet_content = SCRIPTLET_FORMAT % (os.environ['PATH'],
                                                self.target_rootfs,
                                                intercept_dir,
                                                native_root)
        open(self.scriptlet_wrapper, 'w+').write(scriptlet_content)

        bb.note("Note: configuring RPM cross-install scriptlet_wrapper")
        os.chmod(self.scriptlet_wrapper, 0755)
        cmd = 'config --set rpm-extra-macros._cross_scriptlet_wrapper=%s' % \
              self.scriptlet_wrapper
        self._invoke_smart(cmd)

        # Debug to show smart config info
        # bb.note(self._invoke_smart('config --show'))

    def update(self):
        self._invoke_smart('update rpmsys')

    '''
    Install pkgs with smart, the pkg name is oe format
    '''
    def install(self, pkgs, attempt_only=False):

        bb.note("Installing the following packages: %s" % ' '.join(pkgs))
        if len(pkgs) == 0:
            return
        pkgs = self._pkg_translate_oe_to_smart(pkgs, attempt_only)

        if not attempt_only:
            bb.note('to be installed: %s' % ' '.join(pkgs))
            cmd = "%s %s install -y %s" % \
                  (self.smart_cmd, self.smart_opt, ' '.join(pkgs))
            bb.note(cmd)
        else:
            bb.note('installing attempt only packages...')
            bb.note('Attempting %s' % ' '.join(pkgs))
            cmd = "%s %s install --attempt -y %s" % \
                  (self.smart_cmd, self.smart_opt, ' '.join(pkgs))
        try:
            output = subprocess.check_output(cmd.split())
            bb.note(output)
        except subprocess.CalledProcessError as e:
            if not attempt_only:
                bb.note("Unable to install packages. Command %s "
                        "returned %d" % (cmd, e.returncode))

    '''
    Remove pkgs with smart, the pkg name is smart/rpm format
    '''
    def remove(self, pkgs, with_dependencies=True):
        bb.note('to be removed: ' + ' '.join(pkgs))

        if not with_dependencies:
            cmd = "%s -e --nodeps " % self.rpm_cmd
            cmd += "--root=%s " % self.target_rootfs
            cmd += "--dbpath=/var/lib/rpm "
            cmd += "--define='_cross_scriptlet_wrapper %s' " % \
                   self.scriptlet_wrapper
            cmd += "--define='_tmppath /install/tmp' %s" % ' '.join(pkgs)
        else:
            # for pkg in pkgs:
            #   bb.note('Debug: What required: %s' % pkg)
            #   bb.note(self._invoke_smart('query %s --show-requiredby' % pkg))

            cmd = "%s %s remove -y %s" % (self.smart_cmd,
                                          self.smart_opt,
                                          ' '.join(pkgs))

        try:
            bb.note(cmd)
            output = subprocess.check_output(cmd, shell=True)
            bb.note(output)
        except subprocess.CalledProcessError as e:
            bb.note("Unable to remove packages. Command %s "
                    "returned %d" % (cmd, e.returncode))

    def upgrade(self):
        bb.note('smart upgrade')
        self._invoke_smart('upgrade')

    def write_index(self):
        arch_list = list()
        for mlib in self.ml_prefix_list:
            for arch in self.ml_prefix_list[mlib]:
                if arch not in arch_list:
                    arch_list.append(arch)

        sdk_pkg_archs = self.d.getVar('SDK_PACKAGE_ARCHS', True)
        if sdk_pkg_archs is not None:
            arch_list += [i.replace('-', '_') for i in sdk_pkg_archs.split()
                          if i.replace('-', '_') not in arch_list]

        rpm_createrepo = bb.utils.which(os.getenv('PATH'), "createrepo")
        index_cmds = []
        rpm_dirs_found = False
        for arch in arch_list:
            arch_dir = os.path.join(self.deploy_dir, arch)
            if not os.path.isdir(arch_dir):
                continue

            index_cmds.append("%s --update -q %s" % (rpm_createrepo, arch_dir))

            rpm_dirs_found = True

        if not rpm_dirs_found:
            bb.fatal("There are no packages in %s" % self.deploy_dir)

        nproc = multiprocessing.cpu_count()
        pool = bb.utils.multiprocessingpool(nproc)
        results = list(pool.imap(create_index, index_cmds))
        pool.close()
        pool.join()

        for result in results:
            if result is not None:
                bb.fatal(result)

    def remove_packaging_data(self):
        bb.utils.remove(self.image_rpmlib, True)
        bb.utils.remove(os.path.join(self.target_rootfs, 'var/lib/smart'),
                        True)
        bb.utils.remove(os.path.join(self.target_rootfs, 'var/lib/opkg'), True)

        # remove temp directory
        bb.utils.remove(self.d.expand('${IMAGE_ROOTFS}/install'), True)

    def backup_packaging_data(self):
        # Save the rpmlib for increment rpm image generation
        if os.path.exists(self.saved_rpmlib):
            bb.utils.remove(self.saved_rpmlib, True)
        shutil.copytree(self.image_rpmlib,
                        self.saved_rpmlib,
                        symlinks=True)

    def recovery_packaging_data(self):
        # Move the rpmlib back
        if os.path.exists(self.saved_rpmlib):
            if os.path.exists(self.image_rpmlib):
                bb.utils.remove(self.image_rpmlib, True)

            bb.note('Recovery packaging data')
            shutil.copytree(self.saved_rpmlib,
                            self.image_rpmlib,
                            symlinks=True)

    def list_installed(self, format=None):
        cmd = self.rpm_cmd + ' --root ' + self.target_rootfs
        cmd += ' -D "_dbpath /var/lib/rpm" -qa'
        cmd += " --qf '[%{NAME} %{ARCH} %{VERSION} %{PACKAGEORIGIN}\n]'"

        try:
            # bb.note(cmd)
            tmp_output = subprocess.check_output(cmd, shell=True).strip()
            self._unlock_rpm_db()
        except subprocess.CalledProcessError as e:
            bb.fatal("Cannot get the installed packages list. Command %s "
                     "returned %d" % (cmd, e.returncode))

        output = list()
        for line in tmp_output.split('\n'):
            if len(line.strip()) == 0:
                continue
            pkg = line.split()[0]
            arch = line.split()[1]
            ver = line.split()[2]
            pkgorigin = line.split()[3]
            new_pkg, new_arch = self._pkg_translate_smart_to_oe(pkg, arch)

            if format == "arch":
                output.append('%s %s' % (new_pkg, new_arch))
            elif format == "file":
                output.append('%s %s %s' % (new_pkg, pkgorigin, new_arch))
            elif format == "ver":
                output.append('%s %s %s' % (new_pkg, new_arch, ver))
            else:
                output.append('%s' % (new_pkg))

            output.sort()

        return '\n'.join(output)

    '''
    If incremental install, we need to determine what we've got,
    what we need to add, and what to remove...
    The dump_install_solution will dump and save the new install
    solution.
    '''
    def dump_install_solution(self, pkgs):
        bb.note('creating new install solution for incremental install')
        if len(pkgs) == 0:
            return

        pkgs = self._pkg_translate_oe_to_smart(pkgs, False)
        install_pkgs = list()

        cmd = "%s %s install -y --dump %s 2>%s" %  \
              (self.smart_cmd,
               self.smart_opt,
               ' '.join(pkgs),
               self.solution_manifest)
        try:
            # Disable rpmsys channel for the fake install
            self._invoke_smart('channel --disable rpmsys')

            subprocess.check_output(cmd, shell=True)
            with open(self.solution_manifest, 'r') as manifest:
                for pkg in manifest.read().split('\n'):
                    if '@' in pkg:
                        install_pkgs.append(pkg)
        except subprocess.CalledProcessError as e:
            bb.note("Unable to dump install packages. Command %s "
                    "returned %d" % (cmd, e.returncode))
        # Recovery rpmsys channel
        self._invoke_smart('channel --enable rpmsys')
        return install_pkgs

    '''
    If incremental install, we need to determine what we've got,
    what we need to add, and what to remove...
    The load_old_install_solution will load the previous install
    solution
    '''
    def load_old_install_solution(self):
        bb.note('load old install solution for incremental install')
        installed_pkgs = list()
        if not os.path.exists(self.solution_manifest):
            bb.note('old install solution not exist')
            return installed_pkgs

        with open(self.solution_manifest, 'r') as manifest:
            for pkg in manifest.read().split('\n'):
                if '@' in pkg:
                    installed_pkgs.append(pkg.strip())

        return installed_pkgs

    '''
    Dump all available packages in feeds, it should be invoked after the
    newest rpm index was created
    '''
    def dump_all_available_pkgs(self):
        available_manifest = self.d.expand('${T}/saved/available_pkgs.txt')
        available_pkgs = list()
        cmd = "%s %s query --output %s" %  \
              (self.smart_cmd, self.smart_opt, available_manifest)
        try:
            subprocess.check_output(cmd, shell=True)
            with open(available_manifest, 'r') as manifest:
                for pkg in manifest.read().split('\n'):
                    if '@' in pkg:
                        available_pkgs.append(pkg.strip())
        except subprocess.CalledProcessError as e:
            bb.note("Unable to list all available packages. Command %s "
                    "returned %d" % (cmd, e.returncode))

        self.fullpkglist = available_pkgs

        return

    def save_rpmpostinist(self, pkg):
        mlibs = self.d.getVar('MULTILIB_GLOBAL_VARIANTS').split()
        new_pkg = pkg
        # Remove any multilib prefix from the package name
        for mlib in mlibs:
            if mlib in pkg:
                new_pkg = pkg.replace(mlib + '-', '')
                break

        bb.note('  * postponing %s' % new_pkg)
        saved_dir = self.target_rootfs + self.d.expand('${sysconfdir}/rpm-postinsts/') + new_pkg

        cmd = self.rpm_cmd + ' -q --scripts --root ' + self.target_rootfs
        cmd += ' --dbpath=/var/lib/rpm ' + new_pkg
        cmd += ' | sed -n -e "/^postinstall scriptlet (using .*):$/,/^.* scriptlet (using .*):$/ {/.*/p}"'
        cmd += ' | sed -e "/postinstall scriptlet (using \(.*\)):$/d"'
        cmd += ' -e "/^.* scriptlet (using .*):$/d" > %s' % saved_dir

        try:
            bb.note(cmd)
            output = subprocess.check_output(cmd, shell=True).strip()
            bb.note(output)
            os.chmod(saved_dir, 0755)
            self._unlock_rpm_db()
        except subprocess.CalledProcessError as e:
            bb.fatal("Invoke save_rpmpostinist failed. Command %s "
                     "returned %d" % (cmd, e.returncode))

    '''Write common configuration for target usage'''
    def rpm_setup_smart_target_config(self):
        bb.utils.remove(os.path.join(self.target_rootfs, 'var/lib/smart'),
                        True)

        self._invoke_smart('config --set rpm-nolinktos=1')
        self._invoke_smart('config --set rpm-noparentdirs=1')
        for i in self.d.getVar('BAD_RECOMMENDATIONS', True).split():
            self._invoke_smart('flag --set ignore-recommends %s' % i)
        self._invoke_smart('channel --add rpmsys type=rpm-sys -y')

        self._unlock_rpm_db()

    '''
    The rpm db lock files were produced after invoking rpm to query on
    build system, and they caused the rpm on target didn't work, so we
    need to unlock the rpm db by removing the lock files.
    '''
    def _unlock_rpm_db(self):
        # Remove rpm db lock files
        rpm_db_locks = glob.glob('%s/var/lib/rpm/__db.*' % self.target_rootfs)
        for f in rpm_db_locks:
            bb.utils.remove(f, True)


class OpkgPM(PackageManager):
    def __init__(self, d, target_rootfs, config_file, archs):
        super(OpkgPM, self).__init__(d)

        self.target_rootfs = target_rootfs
        self.config_file = config_file
        self.pkg_archs = archs

        self.deploy_dir = self.d.getVar("DEPLOY_DIR_IPK", True)
        self.deploy_lock_file = os.path.join(self.deploy_dir, "deploy.lock")

        self.opkg_cmd = bb.utils.which(os.getenv('PATH'), "opkg-cl")
        self.opkg_args = "-f %s -o %s " % (self.config_file, target_rootfs)
        self.opkg_args += self.d.getVar("OPKG_ARGS", True)

        opkg_lib_dir = self.d.getVar('OPKGLIBDIR', True)
        if opkg_lib_dir[0] == "/":
            opkg_lib_dir = opkg_lib_dir[1:]

        self.opkg_dir = os.path.join(target_rootfs, opkg_lib_dir, "opkg")

        bb.utils.mkdirhier(self.opkg_dir)

        self._create_config()

    def _create_config(self):
        with open(self.config_file, "w+") as config_file:
            priority = 1
            for arch in self.pkg_archs.split():
                config_file.write("arch %s %d\n" % (arch, priority))
                priority += 5

            config_file.write("src oe file:%s\n" % self.deploy_dir)

            for arch in self.pkg_archs.split():
                pkgs_dir = os.path.join(self.deploy_dir, arch)
                if os.path.isdir(pkgs_dir):
                    config_file.write("src oe-%s file:%s\n" % (arch, pkgs_dir))

    def update(self):
        self.deploy_dir_lock()

        cmd = "%s %s update" % (self.opkg_cmd, self.opkg_args)

        try:
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            self.deploy_dir_unlock()
            bb.fatal("Unable to update the package index files. Command %s "
                     "returned %d" % (cmd, e.returncode))

        self.deploy_dir_unlock()

    def install(self, pkgs, attempt_only=False):
        cmd = "%s %s install %s" % (self.opkg_cmd, self.opkg_args, ' '.join(pkgs))

        os.environ['D'] = self.target_rootfs
        os.environ['OFFLINE_ROOT'] = self.target_rootfs
        os.environ['IPKG_OFFLINE_ROOT'] = self.target_rootfs
        os.environ['OPKG_OFFLINE_ROOT'] = self.target_rootfs
        os.environ['INTERCEPT_DIR'] = os.path.join(self.d.getVar('WORKDIR', True),
                                                   "intercept_scripts")
        os.environ['NATIVE_ROOT'] = self.d.getVar('STAGING_DIR_NATIVE', True)

        try:
            bb.note("Installing the following packages: %s" % ' '.join(pkgs))
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            (bb.fatal, bb.note)[attempt_only]("Unable to install packages. "
                                              "Command %s returned %d" %
                                              (cmd, e.returncode))

    def remove(self, pkgs, with_dependencies=True):
        if with_dependencies:
            cmd = "%s %s remove %s" % \
                (self.opkg_cmd, self.opkg_args, ' '.join(pkgs))
        else:
            cmd = "%s %s --force-depends remove %s" % \
                (self.opkg_cmd, self.opkg_args, ' '.join(pkgs))

        try:
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            bb.fatal("Unable to remove packages. Command %s "
                     "returned %d" % (e.cmd, e.returncode))

    def write_index(self):
        arch_vars = ["ALL_MULTILIB_PACKAGE_ARCHS",
                     "SDK_PACKAGE_ARCHS",
                     "MULTILIB_ARCHS"]

        tmpdir = self.d.getVar('TMPDIR', True)
        if os.path.exists(os.path.join(tmpdir, "stamps", "IPK_PACKAGE_INDEX_CLEAN")):
            return

        self.deploy_dir_lock()

        opkg_index_cmd = bb.utils.which(os.getenv('PATH'), "opkg-make-index")

        if not os.path.exists(os.path.join(self.deploy_dir, "Packages")):
            open(os.path.join(self.deploy_dir, "Packages"), "w").close()

        index_cmds = []
        for arch_var in arch_vars:
            archs = self.d.getVar(arch_var, True)
            if archs is None:
                continue

            for arch in archs.split():
                pkgs_dir = os.path.join(self.deploy_dir, arch)
                pkgs_file = os.path.join(pkgs_dir, "Packages")

                if not os.path.isdir(pkgs_dir):
                    continue

                if not os.path.exists(pkgs_file):
                    open(pkgs_file, "w").close()

                index_cmds.append('%s -r %s -p %s -m %s' %
                                  (opkg_index_cmd, pkgs_file, pkgs_file, pkgs_dir))

        if len(index_cmds) == 0:
            self.deploy_dir_unlock()
            bb.fatal("There are no packages in %s!" % self.deploy_dir)

        nproc = multiprocessing.cpu_count()
        pool = bb.utils.multiprocessingpool(nproc)
        results = list(pool.imap(create_index, index_cmds))
        pool.close()
        pool.join()

        self.deploy_dir_unlock()

        for result in results:
            if result is not None:
                bb.fatal(result)

        open(os.path.join(tmpdir, "stamps", "IPK_PACKAGE_INDEX_CLEAN"), "w").close()

    def remove_packaging_data(self):
        bb.utils.remove(self.opkg_dir)
        # create the directory back, it's needed by PM lock
        bb.utils.mkdirhier(self.opkg_dir)

    def list_installed(self, format=None):
        opkg_query_cmd = bb.utils.which(os.getenv('PATH'), "opkg-query-helper.py")

        if format == "arch":
            cmd = "%s %s status | %s -a" % \
                (self.opkg_cmd, self.opkg_args, opkg_query_cmd)
        elif format == "file":
            cmd = "%s %s status | %s -f" % \
                (self.opkg_cmd, self.opkg_args, opkg_query_cmd)
        elif format == "ver":
            cmd = "%s %s status | %s -v" % \
                (self.opkg_cmd, self.opkg_args, opkg_query_cmd)
        else:
            cmd = "%s %s list_installed | cut -d' ' -f1" % \
                (self.opkg_cmd, self.opkg_args)

        try:
            output = subprocess.check_output(cmd, shell=True).strip()
        except subprocess.CalledProcessError as e:
            bb.fatal("Cannot get the installed packages list. Command %s "
                     "returned %d" % (cmd, e.returncode))

        if format == "file":
            tmp_output = ""
            for pkg, pkg_file, pkg_arch in tuple(output.split('\n')):
                full_path = os.path.join(self.deploy_dir, pkg_arch, pkg_file)
                if os.path.exists(full_path):
                    tmp_output += "%s %s %s\n" % (pkg, full_path, pkg_arch)
                else:
                    tmp_output += "%s %s %s\n" % (pkg, pkg_file, pkg_arch)

            output = tmp_output

        return output

    def handle_bad_recommendations(self):
        bad_recommendations = self.d.getVar("BAD_RECOMMENDATIONS", True)
        if bad_recommendations is None:
            return

        status_file = os.path.join(self.opkg_dir, "status")

        cmd = [self.opkg_cmd, self.opkg_args, "info"]

        with open(status_file, "w+") as status:
            for pkg in bad_recommendations.split():
                pkg_info = cmd + [pkg]

                try:
                    output = subprocess.check_output(pkg_info).strip()
                except subprocess.CalledProcessError as e:
                    bb.fatal("Cannot get package info. Command %s "
                             "returned %d" % (' '.join(pkg_info), e.returncode))

                if output == "":
                    bb.note("Requested ignored recommendation $i is "
                            "not a package" % pkg)
                    continue

                for line in output.split('\n'):
                    if line.startswith("Package:") or \
                            line.startswith("Architecture:") or \
                            line.startswith("Version:"):
                        status.write(line)

                status.write("Status: deinstall hold not-installed\n")


class DpkgPM(PackageManager):
    def __init__(self, d, target_rootfs, archs, base_archs, apt_conf_dir=None):
        super(DpkgPM, self).__init__(d)
        self.target_rootfs = target_rootfs
        self.deploy_dir = self.d.getVar('DEPLOY_DIR_DEB', True)
        if apt_conf_dir is None:
            self.apt_conf_dir = self.d.expand("${APTCONF_TARGET}/apt")
        else:
            self.apt_conf_dir = apt_conf_dir
        self.apt_conf_file = os.path.join(self.apt_conf_dir, "apt.conf")
        self.apt_get_cmd = bb.utils.which(os.getenv('PATH'), "apt-get")

        self.apt_args = d.getVar("APT_ARGS", True)

        self._create_configs(archs, base_archs)

    """
    This function will change a package's status in /var/lib/dpkg/status file.
    If 'packages' is None then the new_status will be applied to all
    packages
    """
    def mark_packages(self, status_tag, packages=None):
        status_file = self.target_rootfs + "/var/lib/dpkg/status"

        with open(status_file, "r") as sf:
            with open(status_file + ".tmp", "w+") as tmp_sf:
                if packages is None:
                    tmp_sf.write(re.sub(r"Package: (.*)\nStatus: (.*)(unpacked|installed)",
                                        r"Package: \1\nStatus: \2%s" % status_tag,
                                        sf.read()))
                else:
                    if type(packages).__name__ != "list":
                        raise TypeError("'packages' should be a list object")

                    status = sf.read()
                    for pkg in packages:
                        status = re.sub(r"Package: %s\nStatus: (.*)(unpacked|installed)" % pkg,
                                        r"Package: %s\nStatus: \1%s" % (pkg, status_tag),
                                        status)

                    tmp_sf.write(status)

        os.rename(status_file + ".tmp", status_file)

    """
    Run the pre/post installs for package "package_name". If package_name is
    None, then run all pre/post install scriptlets.
    """
    def run_pre_post_installs(self, package_name=None):
        info_dir = self.target_rootfs + "/var/lib/dpkg/info"
        suffixes = [(".preinst", "Preinstall"), (".postinst", "Postinstall")]
        status_file = self.target_rootfs + "/var/lib/dpkg/status"
        installed_pkgs = []

        with open(status_file, "r") as status:
            for line in status.read().split('\n'):
                m = re.match("^Package: (.*)", line)
                if m is not None:
                    installed_pkgs.append(m.group(1))

        if package_name is not None and not package_name in installed_pkgs:
            return

        os.environ['D'] = self.target_rootfs
        os.environ['OFFLINE_ROOT'] = self.target_rootfs
        os.environ['IPKG_OFFLINE_ROOT'] = self.target_rootfs
        os.environ['OPKG_OFFLINE_ROOT'] = self.target_rootfs
        os.environ['INTERCEPT_DIR'] = os.path.join(self.d.getVar('WORKDIR', True),
                                                   "intercept_scripts")
        os.environ['NATIVE_ROOT'] = self.d.getVar('STAGING_DIR_NATIVE', True)

        failed_pkgs = []
        for pkg_name in installed_pkgs:
            for suffix in suffixes:
                p_full = os.path.join(info_dir, pkg_name + suffix[0])
                if os.path.exists(p_full):
                    try:
                        bb.note("Executing %s for package: %s ..." %
                                 (suffix[1].lower(), pkg_name))
                        subprocess.check_output(p_full)
                    except subprocess.CalledProcessError as e:
                        bb.note("%s for package %s failed with %d" %
                                (suffix[1], pkg_name, e.returncode))
                        failed_pkgs.append(pkg_name)
                        break

        if len(failed_pkgs):
            self.mark_packages("unpacked", failed_pkgs)

    def update(self):
        os.environ['APT_CONFIG'] = self.apt_conf_file

        cmd = "%s update" % self.apt_get_cmd

        try:
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            bb.fatal("Unable to update the package index files. Command %s "
                     "returned %d" % (e.cmd, e.returncode))

    def install(self, pkgs, attempt_only=False):
        os.environ['APT_CONFIG'] = self.apt_conf_file

        cmd = "%s %s install --force-yes --allow-unauthenticated %s" % \
              (self.apt_get_cmd, self.apt_args, ' '.join(pkgs))

        try:
            bb.note("Installing the following packages: %s" % ' '.join(pkgs))
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            (bb.fatal, bb.note)[attempt_only]("Unable to install packages. "
                                              "Command %s returned %d" %
                                              (cmd, e.returncode))

        # rename *.dpkg-new files/dirs
        for root, dirs, files in os.walk(self.target_rootfs):
            for dir in dirs:
                new_dir = re.sub("\.dpkg-new", "", dir)
                if dir != new_dir:
                    os.rename(os.path.join(root, dir),
                              os.path.join(root, new_dir))

            for file in files:
                new_file = re.sub("\.dpkg-new", "", file)
                if file != new_file:
                    os.rename(os.path.join(root, file),
                              os.path.join(root, new_file))


    def remove(self, pkgs, with_dependencies=True):
        if with_dependencies:
            os.environ['APT_CONFIG'] = self.apt_conf_file
            cmd = "%s remove %s" % (self.apt_get_cmd, ' '.join(pkgs))
        else:
            cmd = "%s --admindir=%s/var/lib/dpkg --instdir=%s" \
                  " -r --force-depends %s" % \
                  (bb.utils.which(os.getenv('PATH'), "dpkg"),
                   self.target_rootfs, self.target_rootfs, ' '.join(pkgs))

        try:
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            bb.fatal("Unable to remove packages. Command %s "
                     "returned %d" % (e.cmd, e.returncode))

    def write_index(self):
        tmpdir = self.d.getVar('TMPDIR', True)
        if os.path.exists(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN")):
            return

        pkg_archs = self.d.getVar('PACKAGE_ARCHS', True)
        if pkg_archs is not None:
            arch_list = pkg_archs.split()
        sdk_pkg_archs = self.d.getVar('SDK_PACKAGE_ARCHS', True)
        if sdk_pkg_archs is not None:
            arch_list += sdk_pkg_archs.split()

        dpkg_scanpackages = bb.utils.which(os.getenv('PATH'), "dpkg-scanpackages")
        gzip = bb.utils.which(os.getenv('PATH'), "gzip")

        index_cmds = []
        deb_dirs_found = False
        for arch in arch_list:
            arch_dir = os.path.join(self.deploy_dir, arch)
            if not os.path.isdir(arch_dir):
                continue

            with open(os.path.join(arch_dir, "Release"), "w+") as release:
                release.write("Label: %s" % arch)

            index_cmds.append("cd %s; %s . | %s > Packages.gz" %
                              (arch_dir, dpkg_scanpackages, gzip))

            deb_dirs_found = True

        if not deb_dirs_found:
            bb.fatal("There are no packages in %s" % self.deploy_dir)

        nproc = multiprocessing.cpu_count()
        pool = bb.utils.multiprocessingpool(nproc)
        results = list(pool.imap(create_index, index_cmds))
        pool.close()
        pool.join()

        for result in results:
            if result is not None:
                bb.fatal(result)

        open(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN"), "w+").close()

    def _create_configs(self, archs, base_archs):
        base_archs = re.sub("_", "-", base_archs)

        if os.path.exists(self.apt_conf_dir):
            bb.utils.remove(self.apt_conf_dir, True)

        bb.utils.mkdirhier(self.apt_conf_dir)

        arch_list = []
        for arch in archs.split():
            if not os.path.exists(os.path.join(self.deploy_dir, arch)):
                continue
            arch_list.append(arch)

        with open(os.path.join(self.apt_conf_dir, "preferences"), "w+") as prefs_file:
            priority = 801
            for arch in arch_list:
                prefs_file.write(
                    "Package: *\n"
                    "Pin: release l=%s\n"
                    "Pin-Priority: %d\n\n" % (arch, priority))

                priority += 5

            for pkg in self.d.getVar('PACKAGE_EXCLUDE', True).split():
                prefs_file.write(
                    "Package: %s\n"
                    "Pin: release *\n"
                    "Pin-Priority: -1\n\n" % pkg)

        arch_list.reverse()

        with open(os.path.join(self.apt_conf_dir, "sources.list"), "w+") as sources_file:
            for arch in arch_list:
                sources_file.write("deb file:%s/ ./\n" %
                                   os.path.join(self.deploy_dir, arch))

        with open(self.apt_conf_file, "w+") as apt_conf:
            with open(self.d.expand("${STAGING_ETCDIR_NATIVE}/apt/apt.conf.sample")) as apt_conf_sample:
                for line in apt_conf_sample.read().split("\n"):
                    line = re.sub("Architecture \".*\";",
                                  "Architecture \"%s\";" % base_archs, line)
                    line = re.sub("#ROOTFS#", self.target_rootfs, line)
                    line = re.sub("#APTCONF#", self.apt_conf_dir, line)

                    apt_conf.write(line + "\n")

        target_dpkg_dir = "%s/var/lib/dpkg" % self.target_rootfs
        bb.utils.mkdirhier(os.path.join(target_dpkg_dir, "info"))

        bb.utils.mkdirhier(os.path.join(target_dpkg_dir, "updates"))

        open(os.path.join(target_dpkg_dir, "status"), "w+").close()
        open(os.path.join(target_dpkg_dir, "available"), "w+").close()

    def remove_packaging_data(self):
        bb.utils.remove(os.path.join(self.target_rootfs,
                                     self.d.getVar('opkglibdir', True)), True)
        bb.utils.remove(self.target_rootfs + "/var/lib/dpkg/", True)

    def fix_broken_dependencies(self):
        os.environ['APT_CONFIG'] = self.apt_conf_file

        cmd = "%s %s -f install" % (self.apt_get_cmd, self.apt_args)

        try:
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            bb.fatal("Cannot fix broken dependencies. Command %s "
                     "returned %d" % (cmd, e.returncode))

    def list_installed(self, format=None):
        cmd = [bb.utils.which(os.getenv('PATH'), "dpkg-query"),
               "--admindir=%s/var/lib/dpkg" % self.target_rootfs,
               "-W"]

        if format == "arch":
            cmd.append("-f=${Package} ${PackageArch}\n")
        elif format == "file":
            cmd.append("-f=${Package} ${Package}_${Version}_${Architecture}.deb ${PackageArch}\n")
        elif format == "ver":
            cmd.append("-f=${Package} ${PackageArch} ${Version}\n")
        else:
            cmd.append("-f=${Package}")

        try:
            output = subprocess.check_output(cmd).strip()
        except subprocess.CalledProcessError as e:
            bb.fatal("Cannot get the installed packages list. Command %s "
                     "returned %d" % (' '.join(cmd), e.returncode))

        if format == "file":
            tmp_output = ""
            for pkg, pkg_file, pkg_arch in tuple(output.split('\n')):
                full_path = os.path.join(self.deploy_dir, pkg_arch, pkg_file)
                if os.path.exists(full_path):
                    tmp_output += "%s %s %s\n" % (pkg, full_path, pkg_arch)
                else:
                    tmp_output += "%s %s %s\n" % (pkg, pkg_file, pkg_arch)

            output = tmp_output

        return output

if __name__ == "__main__":
    """
    We should be able to run this as a standalone script, from outside bitbake
    environment.
    """
    """
    TBD
    """
