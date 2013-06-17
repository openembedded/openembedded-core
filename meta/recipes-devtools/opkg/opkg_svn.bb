require opkg.inc

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;protocol=http \
           file://obsolete_automake_macros.patch \
	   file://0001-Fix-libopkg-header-installation.patch \
"

S = "${WORKDIR}/trunk"

SRCREV = "649"
PV = "0.1.8+svnr${SRCPV}"

PR = "${INC_PR}.0"
