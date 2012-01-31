DEPENDS_prepend = "pkgconfig-native "

PKGCONFIGRDEP = "pkgconfig"
PKGCONFIGRDEP_virtclass-native = ""

RDEPENDS_${PN}-dev += "${PKGCONFIGRDEP}"
