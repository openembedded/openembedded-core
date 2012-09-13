PR = "r16"

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
     file://binutils-x86_64_i386_biarch.patch \
     file://libtool-2.4-update.patch \
     file://binutils-2.19.1-ld-sysroot.patch \
     file://libiberty_path_fix.patch \
     file://binutils-poison.patch \
     file://libtool-rpath-fix.patch \
     file://clone-shadow.patch \
     file://binutils-powerpc-e5500.patch \
     file://binutils-armv5e.patch \
     file://mips64-default-ld-emulation.patch \
     file://0001-PR-ld-13470.patch \
     file://rpath-sysroot.patch \
     file://0006-mips-dis.c-print_insn_micromips-Rename-local-variabl.patch \
     file://0019-PR-ld-13468.patch \
     file://0028-Backport-from-mainline.patch \
     file://0035-2011-12-19-Chung-Lin-Tang-cltang-codesourcery.com.patch \
     file://0036-2011-12-19-Chung-Lin-Tang-cltang-codesourcery.com.patch \
     file://0037-2011-12-19-Chung-Lin-Tang-cltang-codesourcery.com.patch \
     file://0038-Copy-from-mainline-to-binutils-2.22-branch.patch \
     file://0039-emulparams-elf32bmip.sh-OTHER_SECTIONS-Put-.mdebug.-.patch \
     file://0052-gas.patch \
     file://0055-Remove-ABI_64_P-check-on-R_X86_64_PCXX.patch \
     file://0078-PR-binutils-13622.patch \
     file://0144-timer.cc-include-unistd.h.patch \
     file://0166-2012-04-27-Doug-Kwan-dougkwan-google.com.patch \
     file://0182-PR-ld-13991.patch \
     "

SRC_URI[md5sum] = "ee0f10756c84979622b992a4a61ea3f5"
SRC_URI[sha256sum] = "6c7af8ed1c8cf9b4b9d6e6fe09a3e1d3d479fe63984ba8b9b26bf356b6313ca9"
