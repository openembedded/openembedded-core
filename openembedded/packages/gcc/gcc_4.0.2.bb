PR = "r1"
DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
MAINTAINER = "Gerald Britton <gbritton@doomcom.org>"

DEFAULT_PREFERENCE_nslu2 = "-1"

inherit autotools gettext

include gcc-package.inc

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
	file://ldflags.patch;patch=1"

include gcc4-build.inc
