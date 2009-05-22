DESCRIPTION = "ConsoleKit is a framework for defining and tracking users, login sessions, and seats."
LICENSE = "GPLv2"
DEPENDS = "dbus"

inherit gnome

SRC_URI = "http://people.freedesktop.org/~mccann/dist/ConsoleKit-${PV}.tar.bz2 \
           file://nopolkit.patch;patch=1"
S = "${WORKDIR}/ConsoleKit-${PV}"

do_stage () {
	autotools_stage_all
}

FILES_${PN} += "${libdir}/ConsoleKit ${datadir}/dbus-1 ${datadir}/PolicyKit"




