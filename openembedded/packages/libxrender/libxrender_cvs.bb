PV = "0.0+cvs${SRCDATE}"
PR = "r1"
SECTION = "libs"
DEPENDS = "renderext x11"
DESCRIPTION = "X Render extension library."
LICENSE = "BSD"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xrender"
S = "${WORKDIR}/Xrender"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
