PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
SECTION = "libs"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xextensions fixesext"
DESCRIPTION = "X Composite extension headers and specification"
DEFAULT_PREFERENCE = "1"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=CompositeExt"
S = "${WORKDIR}/CompositeExt"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
