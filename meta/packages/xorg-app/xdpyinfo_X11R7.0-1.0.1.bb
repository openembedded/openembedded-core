require xorg-app-common.inc

DESCRIPTION = "X display information utility"
LICENSE = "MIT"

# Many X libraries removed from here as they are not hard requirements
DEPENDS += " libxtst libxext virtual/libx11 libxxf86vm libxxf86misc libxi libxrender"

SRC_URI += "file://disable-xkb.patch;patch=1"

EXTRA_OECONF = "--disable-xkb"
