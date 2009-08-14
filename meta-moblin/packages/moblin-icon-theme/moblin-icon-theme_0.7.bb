
SRC_URI = "file://${PN}-${PV}.tar.gz"
PR = "r0"

DEPENDS = "icon-naming-utils-native"

FILES_${PN} += "${datadir}/icons/"

inherit autotools_stage
