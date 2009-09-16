LICENSE= "BSD-X"
SECTION = "x11/libs"

XLIBS_CVS = "${FREEDESKTOP_CVS}/xlibs"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR_HOST}${prefix} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
		mandir=${STAGING_DIR_HOST}${mandir}
}
