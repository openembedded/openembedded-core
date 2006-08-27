require matchbox-panel.inc
PV = "0.9.2+svn${SRCDATE}"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification --enable-dnotify"

S = ${WORKDIR}/${PN}


