from abc import ABCMeta, abstractmethod
from oe.utils import execute_pre_post_process
from oe.utils import contains as base_contains
from oe.package_manager import *
from oe.manifest import *
import shutil
import os
import subprocess
import re


class Rootfs(object):
    """
    This is an abstract class. Do not instantiate this directly.
    """
    __metaclass__ = ABCMeta

    def __init__(self, d, manifest_dir=None):
        self.d = d
        self.pm = None
        self.manifest = None
        self.image_rootfs = self.d.getVar('IMAGE_ROOTFS', True)
        self.deploy_dir_image = self.d.getVar('DEPLOY_DIR_IMAGE', True)

        bb.utils.remove(self.image_rootfs, True)
        bb.utils.remove(self.d.getVar('MULTILIB_TEMP_ROOTFS', True), True)

        self.install_order = ["lgp", "mip", "aop", "mlp"]

    @abstractmethod
    def _create(self):
        pass

    @abstractmethod
    def _get_delayed_postinsts(self):
        pass

    @abstractmethod
    def _save_postinsts(self):
        pass

    @abstractmethod
    def _log_check(self):
        pass

    @abstractmethod
    def _insert_feed_uris(self):
        pass

    @abstractmethod
    def _handle_intercept_failure(self, failed_script):
        pass

    def _exec_shell_cmd(self, cmd):
        fakerootcmd = self.d.getVar('FAKEROOT', True)
        if fakerootcmd is not None:
            exec_cmd = [fakerootcmd, cmd]
        else:
            exec_cmd = cmd

        try:
            subprocess.check_output(exec_cmd)
        except subprocess.CalledProcessError as e:
            return("Command %s returned %d!" % (e.cmd, e.returncode))

        return None

    def create(self):
        bb.note("###### Generate rootfs #######")
        pre_process_cmds = self.d.getVar("ROOTFS_PREPROCESS_COMMAND", True)
        post_process_cmds = self.d.getVar("ROOTFS_POSTPROCESS_COMMAND", True)

        intercepts_dir = os.path.join(self.d.getVar('WORKDIR', True),
                                      "intercept_scripts")

        bb.utils.mkdirhier(self.image_rootfs)

        bb.utils.mkdirhier(self.deploy_dir_image)

        shutil.copytree(self.d.expand("${COREBASE}/scripts/postinst-intercepts"),
                        intercepts_dir)

        shutil.copy(self.d.expand("${COREBASE}/meta/files/deploydir_readme.txt"),
                    self.deploy_dir_image +
                    "/README_-_DO_NOT_DELETE_FILES_IN_THIS_DIRECTORY.txt")

        execute_pre_post_process(self.d, pre_process_cmds)

        # call the package manager dependent create method
        self._create()

        sysconfdir = self.image_rootfs + self.d.getVar('sysconfdir', True)
        bb.utils.mkdirhier(sysconfdir)
        with open(sysconfdir + "/version", "w+") as ver:
            ver.write(self.d.getVar('BUILDNAME', True) + "\n")

        self._run_intercepts()

        execute_pre_post_process(self.d, post_process_cmds)

        if base_contains("IMAGE_FEATURES", "read-only-rootfs",
                         True, False, self.d):
            delayed_postinsts = self._get_delayed_postinsts()
            if delayed_postinsts is not None:
                bb.fatal("The following packages could not be configured"
                         "offline and rootfs is read-only: %s" %
                         delayed_postinsts)

        self._create_devfs()

        self._uninstall_uneeded()

        self._insert_feed_uris()

        self._run_ldconfig()

        self._generate_kernel_module_deps()

    def _uninstall_uneeded(self):
        if base_contains("IMAGE_FEATURES", "package-management",
                         True, False, self.d):
            return

        delayed_postinsts = self._get_delayed_postinsts()
        if delayed_postinsts is None:
            self.pm.remove(["update-rc.d",
                            "base-passwd",
                            self.d.getVar("ROOTFS_BOOTSTRAP_INSTALL", True)],
                           False)

            if os.path.exists(self.d.expand("${IMAGE_ROOTFS}${sysconfdir}/init.d/run-postinsts")):
                self._exec_shell_cmd(["update-rc.d", "-f", "-r",
                                      self.d.getVar('IMAGE_ROOTFS', True),
                                      "run-postinsts", "remove"])
        else:
            self._save_postinsts()

        self.pm.remove_packaging_data()

    def _run_intercepts(self):
        intercepts_dir = os.path.join(self.d.getVar('WORKDIR', True),
                                      "intercept_scripts")

        bb.note("Running intercept scripts:")
        for script in os.listdir(intercepts_dir):
            script_full = os.path.join(intercepts_dir, script)

            if script == "postinst_intercept" or not os.access(script_full, os.X_OK):
                continue

            bb.note("> Executing %s intercept ..." % script)

            try:
                subprocess.check_output(script_full)
            except subprocess.CalledProcessError as e:
                bb.note("WARNING: intercept script '%s' failed with %d!" %
                        (script, e.returncode))

                with open(script_full) as intercept:
                    registered_pkgs = None
                    for line in intercept.read().split("\n"):
                        m = re.match("^##PKGS:(.*)", line)
                        if m is not None:
                            registered_pkgs = m.group(1).strip()
                            break

                    if registered_pkgs is not None:
                        bb.note("The postinstalls for the following packages "
                                "will be postponed for first boot: %s" %
                                registered_pkgs)

                        # call the backend dependent handler
                        self._handle_intercept_failure(registered_pkgs)

    def _run_ldconfig(self):
        if self.d.getVar('LDCONFIGDEPEND', True) is not None:
            bb.note("Executing: ldconfig -r" + self.image_rootfs + "-c new -v")
            self._exec_shell_cmd(['ldconfig', '-r', self.image_rootfs, '-c',
                                  'new', '-v'])

    def _generate_kernel_module_deps(self):
        kernel_abi_ver_file = os.path.join(self.d.getVar('STAGING_KERNEL_DIR', True),
                                           'kernel-abiversion')
        if os.path.exists(kernel_abi_ver_file):
            kernel_ver = open(kernel_abi_ver_file).read()
            modules_dir = os.path.join(self.image_rootfs, 'lib', 'modules', kernel_ver)

            bb.utils.mkdirhier(modules_dir)

            self._exec_shell_cmd(['depmodwrapper', '-a', '-b', self.image_rootfs,
                                  kernel_ver])

    """
    Create devfs:
    * IMAGE_DEVICE_TABLE is the old name to an absolute path to a device table file
    * IMAGE_DEVICE_TABLES is a new name for a file, or list of files, seached
      for in the BBPATH
    If neither are specified then the default name of files/device_table-minimal.txt
    is searched for in the BBPATH (same as the old version.)
    """
    def _create_devfs(self):
        devtable_list = []
        devtable = self.d.getVar('IMAGE_DEVICE_TABLE', True)
        if devtable is not None:
            devtable_list.append(devtable)
        else:
            devtables = self.d.getVar('IMAGE_DEVICE_TABLES', True)
            if devtables is None:
                devtables = 'files/device_table-minimal.txt'
            for devtable in devtables.split():
                devtable_list.append("%s" % bb.utils.which(self.d.getVar('BBPATH', True), devtable))

        for devtable in devtable_list:
            self._exec_shell_cmd(["makedevs", "-r",
                                  self.image_rootfs, "-D", devtable])


class RpmRootfs(Rootfs):
    def __init__(self, manifest):
        super(RpmRootfs, self).__init__(manifest)
    """
    TBD
    """


class DpkgRootfs(Rootfs):
    def __init__(self, d, manifest_dir):
        super(DpkgRootfs, self).__init__(d, manifest_dir)

        self.manifest = DpkgManifest(d, manifest_dir)
        self.pm = DpkgPM(d, d.getVar('IMAGE_ROOTFS', True),
                         d.getVar('PACKAGE_ARCHS', True),
                         d.getVar('DPKG_ARCH', True))

    def _create(self):
        pkgs_to_install = self.manifest.parse_initial_manifest()

        alt_dir = self.d.expand("${IMAGE_ROOTFS}/var/lib/dpkg/alternatives")
        bb.utils.mkdirhier(alt_dir)

        # update PM index files
        self.pm.write_index()

        self.pm.update()

        for pkg_type in self.install_order:
            if pkg_type in pkgs_to_install:
                self.pm.install(pkgs_to_install[pkg_type],
                                [False, True][pkg_type == "aop"])

        self.pm.install_complementary()

        self.pm.fix_broken_dependencies()

        self.pm.mark_packages("installed")

        self.pm.run_pre_post_installs()

    def _get_delayed_postinsts(self):
        pkg_list = []
        with open(self.image_rootfs + "/var/lib/dpkg/status") as status:
            for line in status:
                m_pkg = re.match("^Package: (.*)", line)
                m_status = re.match("^Status:.*unpacked", line)
                if m_pkg is not None:
                    pkg_name = m_pkg.group(1)
                elif m_status is not None:
                    pkg_list.append(pkg_name)

        if len(pkg_list) == 0:
            return None

        return pkg_list

    def _save_postinsts(self):
        num = 0
        for p in self._get_delayed_postinsts():
            dst_postinst_dir = self.d.expand("${IMAGE_ROOTFS}${sysconfdir}/deb-postinsts")
            src_postinst_dir = self.d.expand("${IMAGE_ROOTFS}/var/lib/dpkg/info")

            bb.utils.mkdirhier(dst_postinst_dir)

            if os.path.exists(os.path.join(src_postinst_dir, p + ".postinst")):
                shutil.copy(os.path.join(src_postinst_dir, p + ".postinst"),
                            os.path.join(dst_postinst_dir, "%03d-%s" % (num, p)))

            num += 1

    def _handle_intercept_failure(self, registered_pkgs):
        self.pm.mark_packages("unpacked", registered_pkgs.split())

    def _log_check(self):
        pass

    def _insert_feed_uris(self):
        pass


class OpkgRootfs(Rootfs):
    def __init__(self, manifest):
        super(OpkgRootfs, self).__init__(manifest)
    """
    TBD
    """


def create_rootfs(d, manifest_dir=None):
    env_bkp = os.environ.copy()

    img_type = d.getVar('IMAGE_PKGTYPE', True)
    if img_type == "rpm":
        bb.fatal("RPM backend was not implemented yet...")
    elif img_type == "ipk":
        bb.fatal("IPK backend was not implemented yet...")
    elif img_type == "deb":
        DpkgRootfs(d, manifest_dir).create()

    os.environ.clear()
    os.environ.update(env_bkp)

if __name__ == "__main__":
    """
    We should be able to run this as a standalone script, from outside bitbake
    environment.
    """
    """
    TBD
    """
