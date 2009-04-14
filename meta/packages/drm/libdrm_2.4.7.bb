SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "http://dri.freedesktop.org/libdrm/libdrm-${PV}.tar.bz2"
PR = "r0"
PROVIDES = "drm"
DEPENDS = "libpthread-stubs"

inherit autotools_stage pkgconfig
