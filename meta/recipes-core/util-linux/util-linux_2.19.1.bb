MAJOR_VERSION = "2.19"
PR = "r10"
require util-linux.inc

# note that `lscpu' is under GPLv3+
LICENSE_util-linux-lscpu = "GPLv3+"

SRC_URI += "file://util-linux-ng-replace-siginterrupt.patch \
            file://util-linux-ng-2.16-mount_lock_path.patch \
            file://uclibc-__progname-conflict.patch \      
            file://remove_sigsetmark.patch \
"

SRC_URI[md5sum] = "3eab06f05163dfa65479c44e5231932c"
SRC_URI[sha256sum] = "d3eac4afcc687b3ae1ffedcab2dc12df84c7ba7045cce31386d2b7040a011c7d"

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

do_install_append () {
	sed -i -e '1s,.*,#!${bindir}/env perl,' ${D}${bindir}/chkdupexe
}
