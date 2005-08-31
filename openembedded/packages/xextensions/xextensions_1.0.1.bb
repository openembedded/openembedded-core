SECTION = "x11/libs"
LICENSE= "BSD-X"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "various extension headers."

SRC_URI = "${XLIBS_MIRROR}/xextensions-${PV}.tar.bz2"
S = "${WORKDIR}/xextensions-${PV}"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
