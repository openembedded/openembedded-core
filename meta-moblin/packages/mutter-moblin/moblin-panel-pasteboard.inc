DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "nbtk mutter-moblin gtk+ clutter-1.0"
SRC_URI = "http://download.moblin.org/sources/moblin-panel-pasteboard/0.0/moblin-panel-pasteboard-0.0.2.tar.bz2"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools

