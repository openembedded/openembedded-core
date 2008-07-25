DESCRIPTION = "Util to wait for a dbus signal"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "dbus"
PV = "0.0+svnr${SRCREV}"
PR = "r2"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=dbus-wait;proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools
