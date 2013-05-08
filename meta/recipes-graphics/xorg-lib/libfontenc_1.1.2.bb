SUMMARY = "X font encoding library"

DESCRIPTION = "libfontenc is a library which helps font libraries \
portably determine and deal with different encodings of fonts."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=96254c20ab81c63e65b26f0dbcd4a1c1"

DEPENDS += "zlib xproto font-util"
PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "ad2919764933e075bb0361ad5caa3d19"
SRC_URI[sha256sum] = "a9a4efed3359b2e80161bb66b65038fac145137fa134e71335264cbc23b02f62"
