SECTION = "x11/libs"
LICENSE = "MIT"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "network API translation layer to \
insulate X applications and libraries from OS \
network vageries."

SRC_URI = "${XLIBS_MIRROR}/libXtrans-0.1.tar.bz2"
S = "${WORKDIR}/libXtrans-${PV}"

inherit autotools  pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
