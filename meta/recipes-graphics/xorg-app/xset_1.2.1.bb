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

SRC_URI[md5sum] = "4e0ce390394416c9e2c5cd4d7413ba87"
SRC_URI[sha256sum] = "ec995f7d23109cfa6420ae87c38158f29a2a6f9d0b7df0a1be34e69e165292a1"

CFLAGS += "-D_GNU_SOURCE"
EXTRA_OECONF = "--disable-xkb"
