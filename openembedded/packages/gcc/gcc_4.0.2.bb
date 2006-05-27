DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
MAINTAINER = "Gerald Britton <gbritton@doomcom.org>"
DEPENDS = "mpfr gmp"
PR = "r1"

inherit autotools gettext

include gcc-package.inc

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
	file://ldflags.patch;patch=1"

# uclibc patches below
SRC_URI_append = " file://100-uclibc-conf.patch;patch=1   \
                   file://200-uclibc-locale.patch;patch=1 \
                   file://301-missing-execinfo_h.patch;patch=1 \
                   file://302-c99-snprintf.patch;patch=1  \
                   file://303-c99-complex-ugly-hack.patch;patch=1 \
                   file://800-arm-bigendian.patch;patch=1 \
                   file://zecke-host-cpp-ac-hack.patch;patch=1 "


include gcc4-build.inc
