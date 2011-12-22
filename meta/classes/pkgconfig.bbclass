DEPENDS_prepend = "pkgconfig-native "

PKGCONFIGRDEP = "pkgconfig"
PKGCONFIGRDEP_virtclass-native = ""
PKGCONFIGRDEP_virtclass-nativesdk = "nativesdk-pkgconfig"

RDEPENDS_${PN}-dev += "${PKGCONFIGRDEP}"
