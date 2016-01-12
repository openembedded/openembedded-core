SUMMARY = "ISO language, territory, currency, script codes and their translations"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fbc093901857fcd118f065f900982c24"

SRC_URI = "https://pkg-isocodes.alioth.debian.org/downloads/iso-codes-${PV}.tar.xz"
SRC_URI[md5sum] = "01c02633ede9d6fdf4b5f99d5dbd47b2"
SRC_URI[sha256sum] = "5ef061381e37e9576760df1ad504d4bbc84c270da30512b2891baed9add70729"

# inherit gettext cannot be used, because it adds gettext-native to BASEDEPENDS which
# are inhibited by allarch
DEPENDS = "gettext-native"

inherit allarch autotools

FILES_${PN} += "${datadir}/xml/"
