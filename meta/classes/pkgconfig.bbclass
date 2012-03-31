DEPENDS_prepend = "pkgconfig-native "

PKGCONFIGRDEP = "pkgconfig"
PKGCONFIGRDEP_virtclass-native = ""
PKGCONFIGRDEP_virtclass-nativesdk = "pkgconfig-nativesdk"

RDEPENDS_${PN}-dev += "${PKGCONFIGRDEP}"
