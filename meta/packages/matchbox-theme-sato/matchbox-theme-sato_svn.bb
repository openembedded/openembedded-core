DESCRIPTION = "Matchbox window manager Sato themes"
LICENSE = "GPL"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"
PV = "0.0+svn${SRCDATE}"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=matchbox-sato;proto=http"
S = "${WORKDIR}/matchbox-sato"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/themes"

