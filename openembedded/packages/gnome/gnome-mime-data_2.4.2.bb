LICENSE = "GPL"

inherit pkgconfig autotools

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${PN}/2.4/${PN}-${PV}.tar.bz2"

DEPENDS += "shared-mime-info intltool-native"
RDEPENDS = "shared-mime-info"
