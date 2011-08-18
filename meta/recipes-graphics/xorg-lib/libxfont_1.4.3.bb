SUMMARY = "XFont: X Font rasterisation library"

DESCRIPTION = "libXfont provides various services for X servers, most \
notably font selection and rasterisation (through external libraries \
such as freetype)."

require xorg-lib-common.inc

LICENSE= "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=deeee1c29f32ca774cecf0c83b46bb5c"

DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

PR = "r0"
PE = "1"

#SRC_URI += "file://no-scalable-crash.patch"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "6fb689cfe13d8d9460f4abb5bd88588d"
SRC_URI[sha256sum] = "f79245652901d20092092e942155d32b8dde15527637db3c09a1adc83672e9cc"
