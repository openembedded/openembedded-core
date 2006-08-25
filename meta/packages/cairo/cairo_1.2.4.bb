SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@debian.org>"
DEPENDS = "libx11 libpng fontconfig libxrender"
DESCRIPTION = "Cairo graphics library"
LICENSE = "MPL LGPL"
PR = "r1"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://cairo-fixed.patch;patch=1"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
