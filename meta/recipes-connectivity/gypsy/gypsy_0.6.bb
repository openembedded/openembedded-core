DESCRIPTION = "GPS Controlling Daemon"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 dbus bluez4 dbus-glib libxslt"

SRC_URI = "http://gypsy.freedesktop.org/releases/gypsy-${PV}.tar.gz" 

inherit autotools pkgconfig

FILES_${PN} += "/usr/share/dbus-1/services/"
