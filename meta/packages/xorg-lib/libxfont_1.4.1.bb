DESCRIPTION = "X11 font rasterisation library"

require xorg-lib-common.inc

LICENSE= "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=fe09c513f84613314f80159f1b7b3791"

DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

PR = "r0"
PE = "1"

#SRC_URI += "file://no-scalable-crash.patch;patch=1"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"
