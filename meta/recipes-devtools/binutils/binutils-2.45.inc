LIC_FILES_CHKSUM = "\
    file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674\
    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6\
    file://gas/COPYING;md5=d32239bcb673463ab874e80d47fae504\
    file://include/COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://include/COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://libiberty/COPYING.LIB;md5=a916467b91076e631dd8edb7424769c7\
    file://bfd/COPYING;md5=d32239bcb673463ab874e80d47fae504\
    "

# When upgrading to next major release, ensure that there is no trailing .0, so
# that upstream version check can work correctly.
PV = "2.45"
CVE_VERSION = "2.45"
SRCBRANCH ?= "binutils-2_45-branch"

UPSTREAM_CHECK_GITTAGREGEX = "binutils-(?P<pver>\d+_(\d_?)*)"

CVE_STATUS[CVE-2025-1153] = "cpe-stable-backport: fix available in used git hash"

SRCREV ?= "2bc7af1ff7732451b6a7b09462a815c3284f9613"
BINUTILS_GIT_URI ?= "git://sourceware.org/git/binutils-gdb.git;branch=${SRCBRANCH};protocol=https"
SRC_URI = "\
     ${BINUTILS_GIT_URI} \
     file://0004-Point-scripts-location-to-libdir.patch \
     file://0005-don-t-let-the-distro-compiler-point-to-the-wrong-ins.patch \
     file://0006-warn-for-uses-of-system-directories-when-cross-linki.patch \
     file://0007-fix-the-incorrect-assembling-for-ppc-wait-mnemonic.patch \
     file://0008-Use-libtool-2.4.patch \
     file://0009-Fix-rpath-in-libtool-when-sysroot-is-enabled.patch \
     file://0010-sync-with-OE-libtool-changes.patch \
     file://0011-Check-for-clang-before-checking-gcc-version.patch \
     file://0012-Only-generate-an-RPATH-entry-if-LD_RUN_PATH-is-not-e.patch \
     file://0013-Define-alignof-using-_Alignof-when-using-C11-or-newe.patch \
     file://0014-Remove-duplicate-pe-dll.o-entry-deom-targ_extra_ofil.patch \
"
