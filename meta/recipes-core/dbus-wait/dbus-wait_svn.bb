DESCRIPTION = "A simple tool to wait for a specific signal over DBus"
HOMEPAGE = "http://svn.o-hand.com/repos/misc/trunk"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCREV = "426"
DEPENDS = "dbus"
PV = "0.0+svnr${SRCPV}"
PR = "r2"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=dbus-wait;proto=http"

S = "${WORKDIR}/${BPN}"

inherit autotools
