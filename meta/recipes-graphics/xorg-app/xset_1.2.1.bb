require xorg-app-common.inc

DESCRIPTION = "Utility of setting various user preference options of the display"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=bea81cc9827cdf1af0e12c2b8228cf8d"
DEPENDS += "libxext libxxf86misc libxfontcache libxmu libxp libxau"
PR = "r0"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

CFLAGS += "-D_GNU_SOURCE"
EXTRA_OECONF = "--disable-xkb"
