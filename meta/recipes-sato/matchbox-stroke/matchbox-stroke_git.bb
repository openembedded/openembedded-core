DESCRIPTION = "Matchbox stroke recogniser"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://src/matchbox-stroke.h;endline=12;md5=8ed5c5bbec2321fbf5d31bdd55af03aa"

DEPENDS = "libfakekey expat libxft"
SECTION = "x11/wm"
SRCREV = "8edfd9a2bf1f0d6b28d4afee4bda9d3635f26a0b"
PV = "0.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://single-instance.patch \
           file://configure_fix.patch;maxrev=1819"

S = "${WORKDIR}/git"

inherit autotools pkgconfig gettext

FILES_${PN} = "${bindir}/* \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
		${datadir}/matchbox-stroke"
