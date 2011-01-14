require xorg-app-common.inc

DESCRIPTION = "X display information utility"
LIC_FILES_CHKSUM = "file://COPYING;md5=f3d09e6b9e203a1af489e16c708f4fb3"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxp libxau"
PR = "r0"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

SRC_URI[md5sum] = "c52fda7bbc80e74b7839f29298cb1d77"
SRC_URI[sha256sum] = "fe5bad498ecfbf21f9a2a18eee5ece9de5d52f68878f250db8f575c9b872b5ce"

EXTRA_OECONF = "--disable-xkb"
