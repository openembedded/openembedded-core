SUMMARY = "ISO language, territory, currency, script codes and their translations"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "https://pkg-isocodes.alioth.debian.org/downloads/iso-codes-${PV}.tar.xz"
SRC_URI[md5sum] = "6a3ed31227002f3e40abd793868e78b6"
SRC_URI[sha256sum] = "38ea8c1de7c07d5b4c9603ec65c238c155992a2e2ab0b02725d0926d1ad480c4"

# inherit gettext cannot be used, because it adds gettext-native to BASEDEPENDS which
# are inhibited by allarch
DEPENDS = "gettext-native"

inherit allarch autotools

FILES_${PN} += "${datadir}/xml/"
