require tasks.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.6+svn${SRCDATE}"
S = "${WORKDIR}/trunk"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"
