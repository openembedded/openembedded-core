PR = "r4"

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
     file://libiberty_path_fix.patch \
     file://binutils-poison.patch \
     file://libtool-rpath-fix.patch \
     file://binutils-armv5e.patch \
     file://mips64-default-ld-emulation.patch \
     ${BACKPORT} \
     file://binutils-fix-over-array-bounds-issue.patch \
     file://binutils-xlp-support.patch \
     file://replace_macros_with_static_inline.patch \
     file://0001-Fix-MMIX-build-breakage-from-bfd_set_section_vma-cha.patch \
     "

BACKPORT = "\
     file://backport/0001-doc-binutils.texi-elfedit-Fix-use-of-itemx-in-table.patch \
     file://backport/0001-ld.texinfo-Replace-with-when-it-is-part-of-the-text.patch \
     file://backport/binutils-fix-ineffectual-zero-of-cache.patch \
     file://backport/binutils-replace-strncat-with-strcat.patch  \
     file://backport/0001-config-tc-ppc.c-md_assemble-Do-not-generate-APUinfo-.patch  \
     file://backport/binutils-fix-skip-whitespace-pr14887.patch \
     file://backport/aarch64-crn.patch \
     file://backport/aarch64-movi.patch \
     file://backport/0001-config-tc-ppc.c-PPC_VLE_SPLIT16A-Delete-unused-macro.patch \
     file://backport/0002-config-tc-ppc.c-md_apply_fix-Sign-extend-fieldval-un.patch \
     file://backport/0001-emultempl-elf32.em-gld-EMULATION_NAME-_before_alloca.patch \
     file://backport/0002-emultempl-elf32.em-gld-EMULATION_NAME-_before_alloca.patch \
     file://backport/0003-gold.patch \
"
SRC_URI[md5sum] = "4f8fa651e35ef262edc01d60fb45702e"
SRC_URI[sha256sum] = "fe914e56fed7a9ec2eb45274b1f2e14b0d8b4f41906a5194eac6883cfe5c1097"

