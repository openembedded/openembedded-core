MAJOR_VERSION = "2.17"
PR = "r7"
require util-linux.inc

# note that `lscpu' is under GPLv3+
LICENSE_util-linux-lscpu = "GPLv3+"

SRC_URI += "file://uclibc-compile.patch \
	          file://util-linux-ng-replace-siginterrupt.patch \
                  file://util-linux-ng-2.16-mount_lock_path.patch"

SRC_URI[md5sum] = "4635725a3eef1c57090bac8ea5e082e6"
SRC_URI[sha256sum] = "c9ae801b6a5ab20b7749a278a8bf6830ef53adc5e8b7eb0ac1a9f410c774118f"

# Only lscpu part is gplv3; rest of the code is not, 
# so take out the lscpu parts while running non-gplv3 build.
python () {
    d.setVar("REMOVELSCPU", "no")
    if (d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1:
        # avoid GPLv3 
        d.setVar("REMOVELSCPU", "yes")
        packages = (d.getVar("PACKAGES", False) or "").split()
        if "util-linux-lscpu" in packages:
            packages.remove("util-linux-lscpu")
        d.setVar("PACKAGES", " ".join(packages))

        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://remove-lscpu.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}       

do_remove_lscpu() {
    if [ "x${REMOVELSCPU}" = "xyes" ]; then
         rm -f sys-utils/lscpu.c sys-utils/lscpu.1
         rm -rf tests/ts/lscpu tests/expected/lscpu
    fi
}

addtask remove_lscpu before do_configure after do_patch

# fallocate is glibc 2.10, fallocate64 is glibc 2.11
# we need to disable it for older versions
EXTRA_OECONF += "ac_cv_func_fallocate=no"
EXTRA_OECONF_virtclass-native += "--disable-fallocate --disable-use-tty-group"
