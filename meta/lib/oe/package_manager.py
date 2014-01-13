from abc import ABCMeta, abstractmethod
import os
import subprocess
import multiprocessing
import re


# this can be used by all PM backends to create the index files in parallel
def create_index(arg):
    index_cmd = arg

    try:
        bb.note("Executing '%s' ..." % index_cmd)
        subprocess.check_output(index_cmd, shell=True)
    except subprocess.CalledProcessError as e:
        return("Index creation command %s failed with return code %d!" %
               (' '.join(e.cmd), e.returncode))

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
    This function creates the Packages.gz files in each arch directory in
    DEPLOY_DIR_DEB.
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
    def __init__(self):
        super(RpmPM, self).__init__()

    """
    TBD
    """


class OpkgPM(PackageManager):
    def __init__(self):
        super(OpkgPM, self).__init__()

    """
    TBD
    """


class DpkgPM(PackageManager):
    def __init__(self, d, target_rootfs, archs, dpkg_arch):
        super(DpkgPM, self).__init__(d)
        self.target_rootfs = target_rootfs
        self.deploy_dir = self.d.getVar('DEPLOY_DIR_DEB', True)
        self.apt_conf_dir = self.d.expand("${APTCONF_TARGET}/apt")
        self.apt_conf_file = os.path.join(self.apt_conf_dir, "apt.conf")
        self.apt_get_cmd = bb.utils.which(os.getenv('PATH'), "apt-get")

        self.apt_args = d.getVar("APT_ARGS", True)

        os.environ['APT_CONFIG'] = self.apt_conf_file

        self._create_configs(archs, dpkg_arch)

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
        cmd = "%s update" % self.apt_get_cmd

        try:
            subprocess.check_output(cmd.split())
        except subprocess.CalledProcessError as e:
            bb.fatal("Unable to update the package index files. Command %s "
                     "returned %d" % (e.cmd, e.returncode))

    def install(self, pkgs, attempt_only=False):
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

    def _create_configs(self, archs, dpkg_arch):
        dpkg_arch = re.sub("_", "-", dpkg_arch)

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
                                  "Architecture \"%s\";" % dpkg_arch, line)
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
