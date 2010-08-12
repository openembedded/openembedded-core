DESCRIPTION = "A simple tool to wait for a specific signal over DBus"
HOMEPAGE = "http://svn.o-hand.com/repos/misc/trunk"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504" 
DEPENDS = "dbus"
PV = "0.0+svnr${SRCREV}"
PR = "r2"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=dbus-wait;proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools
