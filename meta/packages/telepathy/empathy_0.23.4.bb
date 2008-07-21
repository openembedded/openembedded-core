DESCRIPTION = "Telepathy based IM client"
HOMEPAGE = "http://blogs.gnome.org/view/xclaesse/2007/04/26/0"
LICENSE = "GPL"
DEPENDS = "telepathy-mission-control libtelepathy telepathy-glib gtk+ gconf libglade eds-dbus"
RRECOMMENDS = "telepathy-gabble"

inherit gnome gtk-icon-cache

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/empathy/0.23/empathy-${PV}.tar.bz2 \
           file://no-gnome-doc.patch;patch=1"

FILES_${PN} += "${datadir}/mission-control/profiles/*.profile \
        ${datadir}/dbus-1/services/*.service \
        ${datadir}/telepathy/managers/*.chandler \
	${datadir}/icons"

PACKAGES =+ "libempathy libempathy-gtk"

FILES_libempathy = "${libdir}/libempathy.so.*"
FILES_libempathy-gtk = "${libdir}/libempathy-gtk.so.*"
