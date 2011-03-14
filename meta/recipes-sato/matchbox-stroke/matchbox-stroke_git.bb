DESCRIPTION = "Matchbox stroke recogniser"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://src/matchbox-stroke.h;endline=12;md5=8ed5c5bbec2321fbf5d31bdd55af03aa"

DEPENDS = "libfakekey expat libxft"
SECTION = "x11/wm"
PV = "0.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.pokylinux.org/${BPN};protocol=git \
           file://single-instance.patch;patch=1 \
           file://configure_fix.patch;patch=1;maxrev=1819 \
           file://dso_linking_change_build_fix.patch "

S = "${WORKDIR}/git"

inherit autotools pkgconfig gettext

FILES_${PN} = "${bindir}/* \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
		${datadir}/matchbox-stroke"
