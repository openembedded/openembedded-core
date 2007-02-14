SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11 libsm libpng fontconfig libxrender"
DESCRIPTION = "Cairo graphics library"
LICENSE = "MPL LGPL"

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points 
require cairo-fpu.inc
EXTRA_OECONF += "${@get_cairo_fpu_setting(bb, d)}"

inherit autotools pkgconfig 

do_stage () {
 	autotools_stage_all
}
