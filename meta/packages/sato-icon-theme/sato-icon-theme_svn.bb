SECTION = "x11"
DESCRIPTION = "Sato Icon Theme"
LICENSE = "LGPL"
DEPENDS = ""
PV = "0.0+svn${SRCDATE}"
PR = "r2"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=sato-icon-theme;proto=http"
S = "${WORKDIR}/sato-icon-theme"

inherit autotools  pkgconfig

FILES_${PN} += "${datadir}"
