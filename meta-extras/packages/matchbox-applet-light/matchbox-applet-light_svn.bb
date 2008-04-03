LICENSE = "GPLv2"
PRIORITY = "optional"
DEPENDS = "libmatchbox gtk+"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=mb-applet-light;proto=http"

S = "${WORKDIR}/mb-applet-light"

inherit autotools pkgconfig
