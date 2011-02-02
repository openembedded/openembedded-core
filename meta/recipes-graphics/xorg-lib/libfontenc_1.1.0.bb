SUMMARY = "X font encoding library"

DESCRIPTION = "libfontenc is a library which helps font libraries \
portably determine and deal with different encodings of fonts."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=96254c20ab81c63e65b26f0dbcd4a1c1"

DEPENDS += "zlib xproto font-util"
PR = "r1"
PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "11d3c292f05a90f6f67840a9e9c3d9b8"
SRC_URI[sha256sum] = "348a1b0142f61afeaafc9497e997d6f10074affed8682e202d019f10170b9cbf"
