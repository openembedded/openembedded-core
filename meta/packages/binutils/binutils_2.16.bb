DESCRIPTION = "A GNU collection of binary utilities"
HOMEPAGE = "http://www.gnu.org/software/binutils/"
SECTION = "devel"
LICENSE = "GPL"
MAINTAINER = "Gerald Britton <gbritton@doomcom.org>"
PR = "r7"

SRC_URI = \
    "http://ftp.gnu.org/gnu/binutils/binutils-${PV}.tar.bz2 \
     file://ld_makefile.patch;patch=1 \
     file://better_file_error.patch;patch=1 \
     file://signed_char_fix.patch;patch=1 \
     file://binutils-2.16-objcopy-rename-errorcode.patch;patch=1 \
     file://binutils-100_cflags_for_build.patch;patch=1"

# uclibc patches
SRC_URI += "file://binutils-2.16-linux-uclibc.patch;patch=1"

# thumb support patches
SRC_URI += "file://binutils-2.16-thumb-trampoline.patch;patch=1"
SRC_URI += "file://binutils-2.16-thumb-glue.patch;patch=1"

S = "${WORKDIR}/binutils-${PV}"
B = "${S}/build.${HOST_SYS}.${TARGET_SYS}"

include binutils.inc
