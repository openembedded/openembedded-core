PR = "r5"
DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
MAINTAINER = "Gerald Britton <gbritton@doomcom.org>"

inherit autotools gettext

include gcc-package.inc

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://gcc34-reverse-compare.patch;patch=1 \
	   file://gcc34-arm-ldm.patch;patch=1 \
	   file://gcc34-arm-ldm-peephole.patch;patch=1 \
	   file://gcc34-arm-tune.patch;patch=1 \
	   file://gcc-3.4.1-uclibc-100-conf.patch;patch=1 \
	   file://gcc-3.4.1-uclibc-200-locale.patch;patch=1 \
	   file://gcc-3.4.0-arm-lib1asm.patch;patch=1 \
	   file://gcc-3.4.0-arm-nolibfloat.patch;patch=1 \
	   file://gcc-3.4.0-arm-bigendian.patch;patch=1 \
	   file://gcc-3.4.0-arm-bigendian-uclibc.patch;patch=1 \
	   file://GCC3.4.0VisibilityPatch.diff;patch=1 \
	   file://15342.patch;patch=1 \
	   file://always-fixincperm.patch;patch=1"

SRC_URI += "file://gcc34-configure.in.patch;patch=1"
SRC_URI += "file://gcc34-thumb-support.patch;patch=1"

include gcc3-build.inc
