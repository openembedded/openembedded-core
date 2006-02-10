SECTION = "x11/wm"
DESCRIPTION = "Matchbox window manager common files"
LICENSE = "GPL"
DEPENDS = "libmatchbox"
PV = "0.9.1+svn${SRCDATE}"
S = "${WORKDIR}/matchbox-common"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=matchbox-common;proto=http"

inherit autotools pkgconfig

EXTRA_OECONF = " --enable-pda-folders "

FILES_${PN} = "${bindir} \
	       ${datadir}/matchbox/vfolders \
	       ${datadir}/pixmaps"
