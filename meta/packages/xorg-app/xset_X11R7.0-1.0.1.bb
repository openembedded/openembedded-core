require xorg-app-common.inc

DESCRIPTION = "user preference utility for X"
MAINTAINER = "Florian Boor <florian.boor@kernelconcepts.de>"
LICENSE = "MIT"

DEPENDS += " libxmu libxext virtual/libx11 libxxf86misc libxfontcache libxp"

SRC_URI += "file://disable-xkb.patch;patch=1"

CFLAGS += "-D_GNU_SOURCE"
EXTRA_OECONF = "--disable-xkb"

