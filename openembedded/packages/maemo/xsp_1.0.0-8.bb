PR = "r0"
LICENSE= "MIT"
DESCRIPTION = "X Server Nokia 770 extensions library"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "libx11 xextensions libxext xpext"

SRC_URI = "http://repository.maemo.org/pool/maemo/ossw/source/x/xsp/${PN}_${PV}.tar.gz"
S = "${WORKDIR}/Xsp"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
	       mandir=${STAGING_DATADIR}/man
}
