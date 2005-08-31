PV = "0.0cvs${CVSDATE}"
LICENSE= "BSD-X"
SECTION = "x11/libs"
DEPENDS = "x11 compositeext xextensions libxfixes"
DESCRIPTION = "X Composite extension library."

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xcomposite"
S = "${WORKDIR}/Xcomposite"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
