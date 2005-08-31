SECTION = "libs"
LICENSE = "Xorg"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xextensions"
DESCRIPTION = "X Resource usage extension headers"

SRC_URI = "${XLIBS_MIRROR}/resourceext-${PV}.tar.bz2"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
