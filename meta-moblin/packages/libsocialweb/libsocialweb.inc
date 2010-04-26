SRC_URI = "http://download.moblin.org/sources/${PN}/0.0/${PN}-0.0.tar.bz2"

DEPENDS = "libsoup-2.4 gconf-dbus librest glib-2.0 twitter-glib gnome-keyring"
RDEPENDS_${PN} = "connman"

inherit autotools

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/libsocialweb/sources/.debug/* ${libdir}/libsocialweb/services/.debug/"

PARALLEL_MAKE = ""

EXTRA_OECONF = "--with-online=connman --disable-shave"
