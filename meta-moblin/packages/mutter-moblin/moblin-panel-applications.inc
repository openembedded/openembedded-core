DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "mutter-moblin nbtk gtk+ glib-2.0 clutter-1.0 gnome-menus"

SRC_URI = "http://download.moblin.org/sources/moblin-panel-applications/0.1/moblin-panel-applications-0.1.2.tar.bz2"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools_stage

