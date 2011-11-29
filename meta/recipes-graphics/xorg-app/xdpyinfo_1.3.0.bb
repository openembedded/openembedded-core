require xorg-app-common.inc

SUMMARY = "Display information utility for X"

DESCRIPTION = "Xdpyinfo is a utility for displaying information about an \
X server. It is used to examine the capabilities of a server, the \
predefined values for various parameters used in communicating between \
clients and the server, and the different types of screens and visuals \
that are available."

LIC_FILES_CHKSUM = "file://COPYING;md5=f3d09e6b9e203a1af489e16c708f4fb3"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxp libxau"
PR = "r0"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

SRC_URI[md5sum] = "1ef08f4c8d0e669c2edd49e4a1bf650d"
SRC_URI[sha256sum] = "23ee4944a32b5701b4379cb420729eb7a4dde54de2b5b006d4747855efd6d73f"

EXTRA_OECONF = "--disable-xkb"
