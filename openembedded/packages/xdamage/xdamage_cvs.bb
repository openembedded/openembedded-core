PV = "0.0cvs${CVSDATE}"
LICENSE= "BSD-X"
SECTION = "x11/libs"
DEPENDS = "x11 damageext libxfixes xproto"
DESCRIPTION = "X Damage extension library."

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xdamage \
	   file://m4.patch;patch=1"
S = "${WORKDIR}/Xdamage"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
