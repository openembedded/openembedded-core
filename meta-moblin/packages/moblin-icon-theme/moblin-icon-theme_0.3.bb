
SRC_URI = "file://${PN}-${PV}.tar.gz"
PR = "r1"

DEPENDS = "icon-naming-utils-native"

FILES_${PN} += "${datadir}/icons/"

inherit autotools_stage

do_install_append () {
	mv "${D}${datadir}/icons/Moblin Netbook" ${D}${datadir}/icons/moblin
}
