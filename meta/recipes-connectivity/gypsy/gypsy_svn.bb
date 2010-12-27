require gypsy.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.0+svnr${SRCPV}"
S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/${PN}/trunk;module=${PN};proto=http \
           file://fixups.patch;patch=1"
