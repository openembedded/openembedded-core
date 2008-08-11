SECTION = "x11"

SRC_URI = "${SOURCEFORGE_MIRROR}/mesa3d/MesaLib-${PV}.tar.bz2 \
	file://mklib-rpath-link.patch;patch=1"
S = "${WORKDIR}/Mesa-${PV}"

LICENSE = "LGPL"

DEPENDS = "makedepend-native xf86vidmodeproto glproto virtual/libx11 libxext libxxf86vm libxi libxmu libice"
