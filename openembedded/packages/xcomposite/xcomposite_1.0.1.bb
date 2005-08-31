SECTION = "x11/libs"
LICENSE= "BSD-X"
DEPENDS = "x11 compositeext xextensions libxfixes"
DESCRIPTION = "X Composite extension library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXcomposite-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXcomposite-${PV}"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
