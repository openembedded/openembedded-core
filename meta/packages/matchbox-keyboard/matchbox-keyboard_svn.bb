DESCRIPTION = "Matchbox virtual keyboard for X11"
LICENSE = "GPL"
DEPENDS = "libfakekey expat libxft"
SECTION = "x11"
MAINTAINER = "Koen Kooi <koen@handhelds.org>"
PV = "0.0+svn${SRCDATE}"
PR="r2"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"


S = ${WORKDIR}/${PN}

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-cairo"

FILES_${PN} = "${bindir} \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
		${datadir}/matchbox-keyboard"
	
