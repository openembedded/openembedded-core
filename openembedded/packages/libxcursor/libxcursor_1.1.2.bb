SECTION = "x11/libs"
LICENSE= "BSD-X"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DESCRIPTION = "X Cursor library"
DEPENDS = "libxfixes"
PR = "r2"

SRC_URI = "${XLIBS_MIRROR}/libXcursor-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXcursor-${PV}"
FILES_${PN} += "${libdir}/libXcursor.so"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
	       mandir=${STAGING_DIR}/man
}
