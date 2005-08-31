SECTION = "x11/libs"
LICENSE= "BSD-X"
DEPENDS = "x11 damageext libxfixes xproto"
DESCRIPTION = "X Damage extension library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXdamage-${PV}.tar.bz2 \
	   file://m4.patch;patch=1 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXdamage-${PV}"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
