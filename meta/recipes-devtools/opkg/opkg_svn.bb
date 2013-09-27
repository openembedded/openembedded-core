require opkg.inc

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;protocol=http \
           file://no-install-recommends.patch \
           file://add-exclude.patch \
           file://opkg-configure.service \
"

S = "${WORKDIR}/trunk"

SRCREV = "653"
PV = "0.1.8+svnr${SRCPV}"

PR = "${INC_PR}.0"
