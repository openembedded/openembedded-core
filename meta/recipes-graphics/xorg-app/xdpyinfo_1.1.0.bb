require xorg-app-common.inc

DESCRIPTION = "X display information utility"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxp libxau"
PR = "r0"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

SRC_URI[md5sum] = "d1d516610316138105cd07064b257c5c"
SRC_URI[sha256sum] = "780d8dfe65653f42ee26d35928ab7f72f5f27ab08eda692fe4baad05126a0631"

EXTRA_OECONF = "--disable-xkb"
