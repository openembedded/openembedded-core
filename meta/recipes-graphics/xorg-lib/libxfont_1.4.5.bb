SUMMARY = "XFont: X Font rasterisation library"

DESCRIPTION = "libXfont provides various services for X servers, most \
notably font selection and rasterisation (through external libraries \
such as freetype)."

require xorg-lib-common.inc

LICENSE= "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=a46c8040f2f737bcd0c435feb2ab1c2c"

DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc zlib"
PROVIDES = "xfont"

PR = "r0"
PE = "1"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "6851da5dae0a6cf5f7c9b9e2b05dd3b4"
SRC_URI[sha256sum] = "bbf96fb80b6b95cdb1dc968085082a6e668193a54cd9d6e2af669909c0cb7170"
