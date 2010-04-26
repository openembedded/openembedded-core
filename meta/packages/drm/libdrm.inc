SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "http://dri.freedesktop.org/libdrm/libdrm-${PV}.tar.bz2"
PROVIDES = "drm"
DEPENDS = "libpthread-stubs udev cairo virtual/libx11"

PACKAGES =+ "libdrm-tests"
FILES_libdrm-tests = "${bindir}/dr* ${bindir}/mode*"

LEAD_SONAME = "libdrm.so"

inherit autotools pkgconfig
