require xorg-app-common.inc

SUMMARY = "Utility for setting various user preference options of the display"

DESCRIPTION = "xset is a utility that is used to set various user \
preference options of the display."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=bea81cc9827cdf1af0e12c2b8228cf8d"
DEPENDS += "libxext libxxf86misc libxfontcache libxmu libxp libxau"
PR = "r0"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

SRC_URI[md5sum] = "d44e0057d6722b25d5a314e82e0b7e7c"
SRC_URI[sha256sum] = "61371c140030b8b05075a1378b34a4d7c438ed9159496a95f10782c6f4aec1e8"

CFLAGS += "-D_GNU_SOURCE"
EXTRA_OECONF = "--disable-xkb"
