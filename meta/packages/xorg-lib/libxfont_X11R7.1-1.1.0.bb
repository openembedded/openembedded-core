require xorg-lib-common.inc

DESCRIPTION = "X font library (used by the X server)."
LICENSE= "BSD-X"
PRIORITY = "optional"

DEPENDS += " freetype fontcacheproto zlib xproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

XORG_PN = "libXfont"

