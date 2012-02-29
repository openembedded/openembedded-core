SUMMARY = "The Cairo 2D vector graphics library"
DESCRIPTION = "Cairo is a multi-platform library providing anti-aliased \
vector-based rendering for multiple target backends. Paths consist \
of line segments and cubic splines and can be rendered at any width \
with various join and cap styles. All colors may be specified with \
optional translucence (opacity/alpha) and combined using the \
extended Porter/Duff compositing algebra as found in the X Render \
Extension."
HOMEPAGE = "http://cairographics.org"
BUGTRACKER = "http://bugs.freedesktop.org"
SECTION = "libs"
LICENSE = "MPL-1 & LGPLv2.1"
X11DEPENDS = "virtual/libx11 libsm libxrender"
DEPENDS = "libpng fontconfig pixman glib-2.0"
PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
           ${@base_contains('DISTRO_FEATURES', 'gtk-directfb', 'gtk-directfb', '', d)}"
PACKAGECONFIG[x11] = "--with-x=yes,--without-x,${X11DEPENDS}"
PACKAGECONFIG[gtk-directfb] = "--enable-directfb=yes,,directfb"

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points 
require cairo-fpu.inc
EXTRA_OECONF += "${@get_cairo_fpu_setting(bb, d)} \
                 --enable-tee \
                "


inherit autotools pkgconfig

# We don't depend on binutils so we need to disable this
export ac_cv_lib_bfd_bfd_openr="no"
