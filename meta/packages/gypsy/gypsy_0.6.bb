DESCRIPTION = "GPS Controlling Daemon"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 dbus bluez-libs dbus-glib libxslt"

SRC_URI = "http://gypsy.freedesktop.org/gypsy-releases/gypsy-${PV}.tar.gz" 

inherit autotools_stage pkgconfig

FILES_${PN} += "/usr/share/dbus-1/services/"
