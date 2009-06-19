
SRC_URI = "file://${PN}-${PV}.tar.bz2"
PR = "r0"

FILES_${PN} =+ "${datadir}/icons/moblin"
DEPENDS = "icon-naming-utils-native"


do_install () {
	install -d ${D}${datadir}/icons/moblin/
	cp -r ${S}/cursors ${D}${datadir}/icons/moblin/
}
