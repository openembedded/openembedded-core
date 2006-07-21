PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT-X"
SECTION = "libs"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "X Render extension headers"
DEFAULT_PREFERENCE = "1"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Render"
S = "${WORKDIR}/Render"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
