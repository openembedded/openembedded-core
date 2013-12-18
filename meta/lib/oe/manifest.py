from abc import ABCMeta, abstractmethod
import os
import re


class Manifest(object):
    """
    This is an abstract class. Do not instantiate this directly.
    """
    __metaclass__ = ABCMeta

    initial_manifest_file_header = \
        "# This file was generated automatically and contains the packages\n" \
        "# passed on to the package manager in order to create the rootfs.\n\n" \
        "# Format:\n" \
        "#  <package_type>,<package_name>\n" \
        "# where:\n" \
        "#   <package_type> can be:\n" \
        "#      'mip' = must install package\n" \
        "#      'aop' = attempt only package\n" \
        "#      'mlp' = multilib package\n" \
        "#      'lgp' = language package\n\n"

    def __init__(self, d, manifest_dir=None):
        self.d = d
        self.image_rootfs = d.getVar('IMAGE_ROOTFS', True)

        if manifest_dir is None:
            self.manifest_dir = self.d.getVar('WORKDIR', True)
        else:
            self.manifest_dir = manifest_dir

        self.initial_manifest = os.path.join(self.manifest_dir, "initial_manifest")
        self.final_manifest = os.path.join(self.manifest_dir, "final_manifest")

    """
    This creates a standard initial manifest for core-image-(minimal|sato|sato-sdk).
    This will be used for testing until the class is implemented properly!
    """
    def _create_dummy_initial(self):
        pkg_list = dict()
        if self.image_rootfs.find("core-image-sato-sdk") > 0:
            pkg_list['mip'] = \
                "packagegroup-core-x11-sato-games packagegroup-base-extended " \
                "packagegroup-core-x11-sato packagegroup-core-x11-base " \
                "packagegroup-core-sdk packagegroup-core-tools-debug " \
                "packagegroup-core-boot packagegroup-core-tools-testapps " \
                "packagegroup-core-eclipse-debug packagegroup-core-qt-demoapps " \
                "apt packagegroup-core-tools-profile psplash " \
                "packagegroup-core-standalone-sdk-target " \
                "packagegroup-core-ssh-openssh dpkg kernel-dev"
            pkg_list['lgp'] = \
                "locale-base-en-us locale-base-en-gb"
        elif self.image_rootfs.find("core-image-sato") > 0:
            pkg_list['mip'] = \
                "packagegroup-core-ssh-dropbear packagegroup-core-x11-sato-games " \
                "packagegroup-core-x11-base psplash apt dpkg packagegroup-base-extended " \
                "packagegroup-core-x11-sato packagegroup-core-boot"
            pkg_list['lgp'] = \
                "locale-base-en-us locale-base-en-gb"
        elif self.image_rootfs.find("core-image-minimal") > 0:
            pkg_list['mip'] = "run-postinsts packagegroup-core-boot"

        with open(self.initial_manifest, "w+") as manifest:
            manifest.write(self.initial_manifest_file_header)

            for pkg_type in pkg_list:
                for pkg in pkg_list[pkg_type].split():
                    manifest.write("%s,%s\n" % (pkg_type, pkg))

    """
    This will create the initial manifest which will be used by Rootfs class to
    generate the rootfs
    """
    @abstractmethod
    def create_initial(self):
        pass

    """
    This creates the manifest after everything has been installed.
    """
    @abstractmethod
    def create_final(self):
        pass

    """
    The following function parses an initial manifest and returns a dictionary
    object with the must install, attempt only, multilib and language packages.
    """
    def parse_initial_manifest(self):
        pkgs = dict()

        with open(self.initial_manifest) as manifest:
            for line in manifest.read().split('\n'):
                comment = re.match("^#.*", line)
                pkg = re.match("^(mip|aop|mlp|lgp),(.*)$", line)

                if comment is not None:
                    continue

                if pkg is not None:
                    pkg_type = pkg.group(1)
                    pkg_name = pkg.group(2)

                    if not pkg_type in pkgs:
                        pkgs[pkg_type] = [pkg_name]
                    else:
                        pkgs[pkg_type].append(pkg_name)

        return pkgs


class RpmManifest(Manifest):
    def create_initial(self):
        self._create_dummy_initial()

    def create_final(self):
        pass


class OpkgManifest(Manifest):
    def create_initial(self):
        self._create_dummy_initial()

    def create_final(self):
        pass


class DpkgManifest(Manifest):
    def create_initial(self):
        var_map = {"PACKAGE_INSTALL": "mip",
                   "PACKAGE_INSTALL_ATTEMPTONLY": "aop",
                   "LINGUAS_INSTALL": "lgp"}

        with open(self.initial_manifest, "w+") as manifest:
            manifest.write(self.initial_manifest_file_header)

            for var in var_map:
                pkg_list = self.d.getVar(var, True)

                if pkg_list is None:
                    continue

                for pkg in pkg_list.split():
                    manifest.write("%s,%s\n" % (var_map[var], pkg))

    def create_final(self):
        pass


def create_manifest(d, final_manifest=False, manifest_dir=None):
    manifest_map = {'rpm': RpmManifest,
                    'ipk': OpkgManifest,
                    'deb': DpkgManifest}

    manifest = manifest_map[d.getVar('IMAGE_PKGTYPE', True)](d, manifest_dir)

    if final_manifest:
        manifest.create_final()
    else:
        manifest.create_initial()


if __name__ == "__main__":
    pass
