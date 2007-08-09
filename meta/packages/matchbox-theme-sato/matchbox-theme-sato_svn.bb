DESCRIPTION = "Matchbox window manager Sato themes"
LICENSE = "CC-BY-SA3"
DEPENDS = "matchbox-wm"
SECTION = "x11/wm"
PV = "0.1+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=matchbox-sato;proto=http"
S = "${WORKDIR}/matchbox-sato"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/themes"
