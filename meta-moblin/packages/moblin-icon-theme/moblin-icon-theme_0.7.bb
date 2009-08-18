
SRC_URI = "file://${PN}-${PV}.tar.gz"
PR = "r1"

DEPENDS = "icon-naming-utils-native"

FILES_${PN} += "${datadir}/icons/"

inherit autotools_stage

do_install_append () {
	ln -s ../apps/gnome-aisleriot.png ${D}${datadir}/icons/moblin/48x48/categories/applications-games.png
}
