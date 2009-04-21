SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "http://dri.freedesktop.org/libdrm/libdrm-${PV}.tar.bz2 \
           file://installtests.patch;patch=1"
PR = "r1"
PROVIDES = "drm"
DEPENDS = "libpthread-stubs udev cairo"

PACKAGES =+ "libdrm-tests"
FILES_libdrm-tests = "${bindir}/dr* ${bindir}/mode*"

inherit autotools_stage pkgconfig
