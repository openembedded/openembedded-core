LICENSE = "GPL"
inherit gnome

DEPENDS += "shared-mime-info intltool-native"
RDEPENDS = "shared-mime-info"

FILES_${PN}-dev += "${datadir}/pkgconfig/*.pc"
