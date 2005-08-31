LICENSE= "BSD-X"
SECTION = "x11/libs"

XLIBS_CVS = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs"

inherit autotools pkgconfig

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
		mandir=${STAGING_DATADIR}/man
}
