LIC_FILES_CHKSUM="\
    file://src-release;endline=17;md5=4830a9ef968f3b18dd5e9f2c00db2d35\
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
     file://fix-pr15815.patch \
     file://fix-pr2404.patch \
     file://fix-pr16476.patch \
     file://fix-pr16428.patch \
     file://replace_macros_with_static_inline.patch \
     file://0001-Fix-MMIX-build-breakage-from-bfd_set_section_vma-cha.patch \
     file://binutils-uninitialised-warning.patch \
     file://binutils_CVE-2014-8484.patch \
     file://binutils_CVE-2014-8485.patch \
     file://binutils_CVE-2014-8501.patch \
     file://binutils_CVE-2014-8502_1.patch \
     file://binutils_CVE-2014-8502.patch \
     file://binutils_CVE-2014-8503.patch \
     file://binutils_CVE-2014-8504.patch \
     file://binutils_CVE-2014-8737.patch \
     file://Fix-tc-i386.c-Werror-logical-not-parentheses-error.patch \
     "

SRC_URI[md5sum] = "e0f71a7b2ddab0f8612336ac81d9636b"
SRC_URI[sha256sum] = "e5e8c5be9664e7f7f96e0d09919110ab5ad597794f5b1809871177a0f0f14137"
