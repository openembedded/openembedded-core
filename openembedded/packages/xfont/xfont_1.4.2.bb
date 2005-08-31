SECTION = "x11/libs"
LICENSE = "BSD-X"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto xtrans zlib"
DESCRIPTION = "X font library (used by the X server)."
PR = "r2"

SRC_URI = "${XLIBS_MIRROR}/libXfont-${PV}.tar.bz2 \
	file://scalable.patch;patch=1 \
	file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXfont-${PV}"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
