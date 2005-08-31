PV = "0.0cvs${CVSDATE}"
SECTION = "x11/libs"
LICENSE = "BSD-X"
MAINTAINER = "Phil Blundell <pb@nexus.co.uk>"
DESCRIPTION = "XCalibrate extension headers"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=XCalibrateExt"
S = "${WORKDIR}/XCalibrateExt"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
