require xorg-app-common.inc

SUMMARY = "Display information utility for X"

DESCRIPTION = "Xdpyinfo is a utility for displaying information about an \
X server. It is used to examine the capabilities of a server, the \
predefined values for various parameters used in communicating between \
clients and the server, and the different types of screens and visuals \
that are available."

LIC_FILES_CHKSUM = "file://COPYING;md5=f3d09e6b9e203a1af489e16c708f4fb3"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxau libxcomposite"
PE = "1"

SRC_URI += "file://disable-xkb.patch"

SRC_URI[md5sum] = "cacc0733f16e4f2a97a5c430fcc4420e"
SRC_URI[sha256sum] = "aef9285069a517ed870e5d8a02d13f7d8a953d7f7220146da563e04c7f128b94"

EXTRA_OECONF = "--disable-xkb"
