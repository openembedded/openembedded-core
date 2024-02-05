LIC_FILES_CHKSUM="\
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

# When upgrading to 2.39, please make sure there is no trailing .0, so
# that upstream version check can work correctly.
PV = "2.38"
CVE_VERSION = "2.38"
SRCBRANCH ?= "binutils-2_38-branch"

UPSTREAM_CHECK_GITTAGREGEX = "binutils-(?P<pver>\d+_(\d_?)*)"

SRCREV ?= "ea5fe5d01e5a182ee7a0eddb54a702109a9f5931"
BINUTILS_GIT_URI ?= "git://sourceware.org/git/binutils-gdb.git;branch=${SRCBRANCH};protocol=git"
SRC_URI = "\
     ${BINUTILS_GIT_URI} \
     file://0004-Point-scripts-location-to-libdir.patch \
     file://0005-Only-generate-an-RPATH-entry-if-LD_RUN_PATH-is-not-e.patch \
     file://0006-don-t-let-the-distro-compiler-point-to-the-wrong-ins.patch \
     file://0007-warn-for-uses-of-system-directories-when-cross-linki.patch \
     file://0008-fix-the-incorrect-assembling-for-ppc-wait-mnemonic.patch \
     file://0009-Use-libtool-2.4.patch \
     file://0010-Fix-rpath-in-libtool-when-sysroot-is-enabled.patch \
     file://0011-sync-with-OE-libtool-changes.patch \
     file://0012-Check-for-clang-before-checking-gcc-version.patch \
     file://0013-Avoid-as-info-race-condition.patch \
     file://0014-CVE-2019-1010204.patch \
     file://0015-CVE-2022-38533.patch \
     file://0016-CVE-2022-38126.patch \
     file://0017-CVE-2022-38127-1.patch \
     file://0017-CVE-2022-38127-2.patch \
     file://0017-CVE-2022-38127-3.patch \
     file://0017-CVE-2022-38127-4.patch \
     file://0018-CVE-2022-38128-1.patch \
     file://0018-CVE-2022-38128-2.patch \
     file://0018-CVE-2022-38128-3.patch \
     file://0019-CVE-2022-4285.patch \
     file://0020-CVE-2023-22608-1.patch \
     file://0020-CVE-2023-22608-2.patch \
     file://0020-CVE-2023-22608-3.patch \
     file://0021-CVE-2023-1579-1.patch \
     file://0021-CVE-2023-1579-2.patch \
     file://0021-CVE-2023-1579-3.patch \
     file://0021-CVE-2023-1579-4.patch \
     file://0022-CVE-2023-25584-1.patch \
     file://0022-CVE-2023-25584-2.patch \
     file://0022-CVE-2023-25584-3.patch \
     file://0023-CVE-2023-25585.patch \
     file://0026-CVE-2023-1972.patch \
     file://0025-CVE-2023-25588.patch \
     file://0027-CVE-2022-47008.patch \
     file://0028-CVE-2022-47011.patch \
     file://0029-CVE-2022-48065-1.patch \
     file://0029-CVE-2022-48065-2.patch \
     file://0029-CVE-2022-48065-3.patch \
     file://0030-CVE-2022-44840.patch \
     file://0031-CVE-2022-45703-1.patch \
     file://0031-CVE-2022-45703-2.patch \
     file://0031-CVE-2022-47695.patch \
     file://CVE-2022-48063.patch \
     file://0032-CVE-2022-47010.patch \
     file://0033-CVE-2022-47007.patch \
     file://0034-CVE-2022-48064.patch \
     file://0035-CVE-2023-39129.patch \
     file://0036-CVE-2023-39130.patch \
"
S  = "${WORKDIR}/git"
