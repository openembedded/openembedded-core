SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@debian.org>"
DEPENDS = "x11 libpng fontconfig libxrender"
DESCRIPTION = "Cairo graphics library"
LICENSE = "MPL LGPL"
PR = "r1"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
