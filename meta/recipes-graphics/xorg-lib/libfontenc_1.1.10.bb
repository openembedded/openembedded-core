SUMMARY = "X font encoding library"

DESCRIPTION = "libfontenc is a library which helps font libraries \
portably determine and deal with different encodings of fonts."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=96254c20ab81c63e65b26f0dbcd4a1c1"

DEPENDS += "zlib xorgproto font-util"

XORGBUILDCLASS = "meson"

PE = "1"

SRC_URI[sha256sum] = "daa186a49e3cc2b6fc585ca09971351e6f010f618abc6a164eda82e42e4aa4d6"

BBCLASSEXTEND = "native"
