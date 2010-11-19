SUMMARY = "A 2D graphics library"
DESCRIPTION = "Cairo is a 2D graphics library designed to provide high-quality display \
and print output. Cairo is designed to produce consistent output on all output media while \
taking advantage of display hardware acceleration when available."
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
