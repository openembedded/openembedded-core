require sato-icon-theme.inc

DEPENDS += "icon-naming-utils-native"

PR = "r2"

SRC_URI = "http://pokylinux.org/releases/sato/${BPN}-${PV}.tar.gz \
           file://iconpath-option.patch"

SRC_URI[md5sum] = "86a847f3128a43a9cf23b7029a656f50"
SRC_URI[sha256sum] = "0b0a2807a6a96918ac799a86094ec3e8e2c892be0fd679a4232c2a77f2f61732"
