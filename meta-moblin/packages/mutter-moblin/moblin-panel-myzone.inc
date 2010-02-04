DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "mx mutter-moblin gtk+ libsocialweb gconf libjana"
SRC_URI = "http://download.moblin.org/sources/moblin-panel-myzone/0.0/moblin-panel-myzone-0.0.12.tar.bz2"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools_stage

