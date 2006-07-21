PV = "0.0+cvs${SRCDATE}"
LICENSE = "BSD-X"
SECTION = "libs"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DESCRIPTION = "XRecord extension protocol bits"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=RecordExt"
S = "${WORKDIR}/RecordExt"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
