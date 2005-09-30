DESCRIPTION = "X Input Extension library"
LICENSE = "MIT-X"
SECTION = "x11/libs"
DEPENDS = "xproto x11 xextensions"
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXi-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXi-${PV}"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}

