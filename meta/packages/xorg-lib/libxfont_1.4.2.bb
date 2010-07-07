DESCRIPTION = "X11 font rasterisation library"

require xorg-lib-common.inc

LICENSE= "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=deeee1c29f32ca774cecf0c83b46bb5c"

DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

PR = "r0"
PE = "1"

#SRC_URI += "file://no-scalable-crash.patch;patch=1"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"
