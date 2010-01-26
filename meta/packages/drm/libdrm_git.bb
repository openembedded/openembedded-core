SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"
PROVIDES = "drm"

S = ${WORKDIR}/git

DEPENDS = "libpthread-stubs"

PV = "2.4.15+git${SRCREV}"
PR = "r0"

LEAD_SONAME = "libdrm.so"

inherit autotools_stage pkgconfig
