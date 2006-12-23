#This is a development snapshot, so lets hint OE to use the releases
DEFAULT_PREFERENCE = "-1"

SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11 libsm libpng fontconfig libxrender"
DESCRIPTION = "Cairo graphics library"
LICENSE = "MPL LGPL"

SRC_URI = "http://cairographics.org/snapshots/cairo-${PV}.tar.gz"

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points 
require cairo-fpu.inc
EXTRA_OECONF += "${@get_cairo_fpu_setting(bb, d)}"

inherit autotools pkgconfig 

do_stage () {
 	autotools_stage_all
}
