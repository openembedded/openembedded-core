require xorg-app-common.inc

DESCRIPTION = "X display information utility"
LICENSE = "MIT"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxp libxau"
PR = "r1"
PE = "1"

SRC_URI += "file://disable-xkb.patch;patch=1"

EXTRA_OECONF = "--disable-xkb"
