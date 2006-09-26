LICENSE = "GPLv2"
PRIORITY = "optional"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
PV = "0.0+svn${SRCDATE}"
DEPENDS = "libmatchbox gtk+"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=mb-applet-light;proto=http"

S = ${WORKDIR}/mb-applet-light

inherit autotools pkgconfig

