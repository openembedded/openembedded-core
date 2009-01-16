SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "http://dri.freedesktop.org/libdrm/libdrm-${PV}.tar.bz2 \
           file://poulsbo.patch;patch=1"
PROVIDES = "drm libdrm"

S = ${WORKDIR}/libdrm-${PV}

DEPENDS = "libpthread-stubs"

PR = "r7"

COMPATIBLE_MACHINE = "menlow"

LEAD_SONAME = "libdrm.so"

inherit autotools_stage pkgconfig
