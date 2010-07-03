DESCRIPTION = "Cairo graphics library"
HOMEPAGE = "http://cairographics.org"
BUGTRACKER = "http://bugs.freedesktop.org"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "MPLv1.1 & LGPLv2.1"
DEPENDS = "virtual/libx11 libsm libpng fontconfig libxrender pixman"

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points 
require cairo-fpu.inc
EXTRA_OECONF += "${@get_cairo_fpu_setting(bb, d)}"

inherit autotools pkgconfig
