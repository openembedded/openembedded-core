require xorg-lib-common.inc

PR = "r1"
SRC_URI += "file://no-scalable-crash.patch;patch=1"


DESCRIPTION = "X font library (used by the X server)."
LICENSE= "BSD-X"
PRIORITY = "optional"

DEPENDS += " freetype fontcacheproto zlib xproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

XORG_PN = "libXfont"

