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

SRC_URI = "\
     ${GNU_MIRROR}/binutils/binutils-${PV}.tar.bz2 \
     file://binutils-uclibc-100-uclibc-conf.patch \
     file://binutils-uclibc-300-001_ld_makefile_patch.patch \
     file://binutils-uclibc-300-006_better_file_error.patch \
     file://binutils-uclibc-300-012_check_ldrunpath_length.patch \
     file://binutils-uclibc-gas-needs-libm.patch \
     file://libtool-2.4-update.patch \
     file://libiberty_path_fix.patch \
     file://binutils-poison.patch \
     file://libtool-rpath-fix.patch \
     file://binutils-armv5e.patch \
     file://mips64-default-ld-emulation.patch \
     file://binutils-xlp-support.patch \
     file://0001-Fix-an-internal-error-in-do_print_to_mapfile-seen-wi.patch \
     "
SRC_URI[md5sum] = "d9f3303f802a5b6b0bb73a335ab89d66"
SRC_URI[sha256sum] = "22defc65cfa3ef2a3395faaea75d6331c6e62ea5dfacfed3e2ec17b08c882923"

