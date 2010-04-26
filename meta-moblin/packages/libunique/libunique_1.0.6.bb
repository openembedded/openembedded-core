SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libunique/1.0/libunique-1.0.6.tar.bz2"
PR = "r0"

DEPENDS = "glib-2.0 gtk+ dbus"

S = "${WORKDIR}/unique-${PV}"

inherit autotools
