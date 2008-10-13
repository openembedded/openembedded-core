SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"
PROVIDES = "drm"

S = ${WORKDIR}/git

PR = "r0"
PV = "2.4.0+git${SRCREV}"

LEAD_SONAME = "libdrm.so"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
