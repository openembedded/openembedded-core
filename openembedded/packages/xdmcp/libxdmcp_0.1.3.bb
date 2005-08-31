SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE= "MIT"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto"
PROVIDES = "xdmcp"
DESCRIPTION = "X Display Manager Control Protocol library."
PR = "r1"
S = "${WORKDIR}/libXdmcp-${PV}"

SRC_URI = "${XLIBS_MIRROR}/libXdmcp-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
