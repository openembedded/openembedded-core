PV = "0.0+svn${SRCDATE}"
DEPENDS = "gtk+ startup-notification"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification"

S = ${WORKDIR}/${PN}

inherit autotools

