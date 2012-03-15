SUMMARY = "X font encoding library"

DESCRIPTION = "libfontenc is a library which helps font libraries \
portably determine and deal with different encodings of fonts."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=96254c20ab81c63e65b26f0dbcd4a1c1"

DEPENDS += "zlib xproto font-util"
PR = "r0"
PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "a2a861f142c3b4367f14fc14239fc1f7"
SRC_URI[sha256sum] = "de72812f1856bb63bd2226ec8c2e2301931d3c72bd0f08b0d63a0cdf0722017f"
