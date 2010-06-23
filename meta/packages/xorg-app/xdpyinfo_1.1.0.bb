require xorg-app-common.inc

DESCRIPTION = "X display information utility"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxp libxau"
PR = "r0"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

EXTRA_OECONF = "--disable-xkb"
