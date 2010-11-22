SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libunique/1.1/libunique-${PV}.tar.bz2"
PR = "r1"

DEPENDS = "glib-2.0 gtk+ dbus"

LICENSE = "LGPLv2.1"

#S = "${WORKDIR}/unique-${PV}"

inherit autotools
