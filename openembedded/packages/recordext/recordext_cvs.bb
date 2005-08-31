PV = "0.0cvs${CVSDATE}"
LICENSE = "BSD-X"
SECTION = "libs"
MAINTAINER = "Phil Blundell <pb@nexus.co.uk>"
DESCRIPTION = "XRecord extension protocol bits"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=RecordExt"
S = "${WORKDIR}/RecordExt"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
