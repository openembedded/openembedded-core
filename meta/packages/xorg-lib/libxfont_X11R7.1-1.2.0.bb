require xorg-lib-common.inc

PR = "r1"

DESCRIPTION = "X font library (used by the X server)."
LICENSE= "BSD-X"
PRIORITY = "optional"

SRC_URI = "${XORG_MIRROR}/individual/lib/libXfont-1.2.0.tar.bz2"
SRC_URI += "file://no-scalable-crash.patch;patch=1"

DEPENDS += " freetype fontcacheproto zlib xproto xtrans fontsproto libfontenc"
PROVIDES = "xfont"

XORG_PN = "libXfont"

S = "${WORKDIR}/libXfont-1.2.0"
