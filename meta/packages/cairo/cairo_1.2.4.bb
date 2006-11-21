SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11 libpng fontconfig libxrender"
DESCRIPTION = "Cairo graphics library"
LICENSE = "MPL LGPL"
PR = "r1"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://cairo-fixed.patch;patch=1"
#	   file://0001-Add-autoconf-macro-AX_C_FLOAT_WORDS_BIGENDIAN.diff;patch=1
#	   file://0002-Change-_cairo_fixed_from_double-to-use-the-magic-number-technique.diff;patch=1

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
