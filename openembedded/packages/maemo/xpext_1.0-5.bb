PR = "r0"
LICENSE= "MIT"
DESCRIPTION = "X Server Nokia 770 extensions library"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "x11 xextensions xext"

SRC_URI = "http://repository.maemo.org/pool/maemo/ossw/source/x/${PN}/${PN}_${PV}.tar.gz \
           file://auxdir.patch;patch=1;pnum=0"
S = "${WORKDIR}/xpext-1.0"

inherit autotools pkgconfig 

do_configure_prepend () {
  cd ${S}
  chmod +x ./autogen.sh
  ./autogen.sh
}

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
	       mandir=${STAGING_DATADIR}/man
}
