require xorg-app-common.inc

DESCRIPTION = "user preference utility for X"
LICENSE = "MIT"
DISTRO_PN_ALIAS = "Fedora=xorg-x11-server-utils;Ubuntu=x11-xserver-utils;Debian=x11-xserver-utils;Opensuse=xorg-x11"
DEPENDS += "libxext libxxf86misc libxfontcache libxmu libxp libxau"
PE = "1"

SRC_URI += "file://disable-xkb.patch;patch=1"

CFLAGS += "-D_GNU_SOURCE"
EXTRA_OECONF = "--disable-xkb"
