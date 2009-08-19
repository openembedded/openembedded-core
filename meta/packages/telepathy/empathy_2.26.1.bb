DESCRIPTION = "Empathy: a Telepathy based IM client"
HOMEPAGE = "http://blogs.gnome.org/view/xclaesse/2007/04/26/0"
LICENSE = "GPL"
DEPENDS = "libcanberra telepathy-farsight gnome-doc-utils python-native telepathy-python telepathy-mission-control libtelepathy telepathy-glib gtk+ gconf libglade eds-dbus"
RDEPENDS = "telepathy-mission-control"
RRECOMMENDS = "telepathy-gabble"

inherit gnome

PARALLEL_MAKE = ""

PACKAGES =+ "empathy-scrollkeeper-junk"
FILES_empathy-scrollkeeper-junk = "/var/lib/scrollkeeper"

FILES_${PN} += "${datadir}/mission-control/profiles/*.profile \
        ${datadir}/dbus-1/services/*.service \
        ${datadir}/telepathy/managers/*.chandler \
	${datadir}/icons \
	${libdir}/python*"

FILES_${PN}-dbg += "${libdir}/python*/*/.debug"

