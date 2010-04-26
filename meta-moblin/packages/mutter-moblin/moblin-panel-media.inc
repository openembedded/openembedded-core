DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "mutter-moblin bickley bognor-regis gtk+"
SRC_URI = "http://download.moblin.org/sources/moblin-panel-media/0.0/moblin-panel-media-0.0.7.tar.bz2"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools

