DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "mx mutter-moblin dbus-glib clutter-1.0 telepathy-mission-control libsocialweb"

SRC_URI = "http://download.moblin.org/sources/moblin-panel-status/0.0/moblin-panel-status-0.0.9.tar.bz2"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools_stage

