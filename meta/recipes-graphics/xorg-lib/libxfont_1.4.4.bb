SUMMARY = "XFont: X Font rasterisation library"

DESCRIPTION = "libXfont provides various services for X servers, most \
notably font selection and rasterisation (through external libraries \
such as freetype)."

require xorg-lib-common.inc

LICENSE= "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=a46c8040f2f737bcd0c435feb2ab1c2c"

DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

PR = "r0"
PE = "1"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "f9942bc818d39094d7295b156a729393"
SRC_URI[sha256sum] = "a2065f5f66882f7a9cb0eb674e16d284da48e449af443eda272e99832be8239a"
