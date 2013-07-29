require opkg.inc

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;protocol=http \
           file://obsolete_automake_macros.patch \
"

S = "${WORKDIR}/trunk"

SRCREV = "650"
PV = "0.1.8+svnr${SRCPV}"

PR = "${INC_PR}.0"
