DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "nbtk mutter-moblin anerley gtk+ glib-2.0"

SRC_URI = "http://download.moblin.org/sources/moblin-panel-people/0.0/moblin-panel-people-0.0.9.tar.bz2"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools

