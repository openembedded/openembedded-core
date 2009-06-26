SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"
PROVIDES = "drm"

S = ${WORKDIR}/git

DEPENDS = "libpthread-stubs"

PR = "r0"
PV = "2.4.7+git${SRCREV}"

LEAD_SONAME = "libdrm.so"

inherit autotools_stage pkgconfig
