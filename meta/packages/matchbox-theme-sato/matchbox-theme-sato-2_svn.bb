DESCRIPTION = "Matchbox window manager 2 Sato themes"
LICENSE = "CC-BY-SA3"
DEPENDS = "matchbox-wm-2"
SECTION = "x11/wm"
PV = "0.1+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=matchbox-sato;proto=http"
S = "${WORKDIR}/matchbox-sato"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-matchbox-1 --enable-matchbox-2"

FILES_${PN} += "${datadir}/themes"
