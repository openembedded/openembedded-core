MAJOR_VERSION = "2.21"
PR = "r4"
require util-linux.inc

# note that `lscpu' is under GPLv3+
LICENSE_${PN}-lscpu = "GPLv3+"

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \
	    file://configure-sbindir.patch \
"

SRC_URI[md5sum] = "b75b3cfecb943f74338382fde693c2c3"
SRC_URI[sha256sum] = "066f9d8e51bfabd809d266edcd54eefba1cdca57725b95c074fd47fe6fba3d30"

# Only lscpu part is gplv3; rest of the code is not, 
# so take out the lscpu parts while running non-gplv3 build.
# The removal of the package should now occur during
# the build if INCOMPATIBLE_LICENSE is set to GPLv3

python () {
    d.setVar("REMOVELSCPU", "no")
    if (d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1:
        # avoid GPLv3
        d.setVar("REMOVELSCPU", "yes")
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

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=as"
EXTRA_OECONF_virtclass-native += "--disable-fallocate --disable-use-tty-group"
