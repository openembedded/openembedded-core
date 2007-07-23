require xorg-lib-common.inc

DESCRIPTION = "X11 font rasterisation library"
LICENSE= "BSD-X"
DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"
PR = "r2"
PE = "1"

SRC_URI += "file://no-scalable-crash.patch;patch=1 \
            file://builtinreaddirectory-no-side-effect.patch;patch=1"

XORG_PN = "libXfont"
