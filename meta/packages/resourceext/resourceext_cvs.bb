PV = "0.0+cvs${SRCDATE}"
SECTION = "libs"
LICENSE = "Xorg"
DEPENDS = "xextensions"
DESCRIPTION = "X Resource usage extension headers"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=ResourceExt"
S = "${WORKDIR}/ResourceExt"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
