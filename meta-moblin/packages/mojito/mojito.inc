SRC_URI = "http://download.moblin.org/sources/mojito/0.21/mojito-0.21.7.tar.bz2"

DEPENDS = "libsoup-2.4 gconf-dbus librest glib-2.0 twitter-glib gnome-keyring"
RDEPENDS_${PN} = "connman"

inherit autotools

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/mojito/sources/.debug/* ${libdir}/mojito/services/.debug/"

PARALLEL_MAKE = ""

EXTRA_OECONF = "--with-online=connman --disable-shave"
