DESCRIPTION = "Matchbox keyboard"
LICENSE = "GPL"
DEPENDS = "xtst"
SECTION = "x11/wm"
PR="r1"
PV = "0.0+svn${SRCDATE}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext

FILES_${PN} = "${libdir} \
	       ${datadir}/applications \
	       ${datadir}/pixmaps"

do_stage () {
	   install -d ${STAGING_INCDIR}/fakekey	
           install -m 0644 ${S}/fakekey/fakekey.h ${STAGING_INCDIR}/fakekey
	    oe_libinstall -so -C src libfakekey ${STAGING_LIBDIR}		
}

