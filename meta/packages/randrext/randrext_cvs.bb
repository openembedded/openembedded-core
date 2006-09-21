PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
SECTION = "libs"
DESCRIPTION = "X Resize and Rotate extension headers"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Randr"
S = "${WORKDIR}/Randr"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
