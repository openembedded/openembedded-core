DESCRIPTION = "Util to wait for a dbus signal"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "dbus"
PV = "0.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=dbus-wait;proto=http"

S = "${WORKDIR}/${PN}"

CFLAGS += "`pkg-config --libs dbus-1`"

do_install () {
	install -d ${D}${bindir}/
	install ${S}/dbus-wait ${D}${bindir}/
}
