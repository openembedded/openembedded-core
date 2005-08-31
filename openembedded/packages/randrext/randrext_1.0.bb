SECTION = "libs"
LICENSE= "BSD-X"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "X Resize and Rotate extension headers"

SRC_URI = "${XLIBS_MIRROR}/randrext-${PV}.tar.bz2"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
