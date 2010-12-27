DESCRIPTION = "Matchbox stroke recogniser"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://src/matchbox-stroke.h;endline=12;md5=8ed5c5bbec2321fbf5d31bdd55af03aa"

DEPENDS = "libfakekey expat libxft"
SECTION = "x11/wm"
PV = "0.0+svnr${SRCPV}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
           file://single-instance.patch;patch=1 \
           file://configure_fix.patch;patch=1;maxrev=1819 "

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext

FILES_${PN} = "${bindir}/* \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
		${datadir}/matchbox-stroke"
