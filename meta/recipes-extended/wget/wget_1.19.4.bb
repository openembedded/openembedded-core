SRC_URI = "${GNU_MIRROR}/wget/wget-${PV}.tar.gz \
           file://0001-Unset-need_charset_alias-when-building-for-musl.patch \
           file://0002-improve-reproducibility.patch \
          "

SRC_URI[md5sum] = "a2a2c1dc4ac5003fc25a8e60b4a9464e"
SRC_URI[sha256sum] = "93fb96b0f48a20ff5be0d9d9d3c4a986b469cb853131f9d5fe4cc9cecbc8b5b5"

require wget.inc
