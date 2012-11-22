require opkg.inc

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;protocol=http \
"

S = "${WORKDIR}/trunk"

SRCREV = "649"
PV = "0.1.8+svnr${SRCPV}"

PR = "${INC_PR}.0"
