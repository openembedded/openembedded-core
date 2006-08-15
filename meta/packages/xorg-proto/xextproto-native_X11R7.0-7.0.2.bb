DESCRIPTION = "X protocol headers"
SECTION = "x11/libs"
LICENSE= "MIT-X"
#MAINTAINER = ""

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/protto/xextproto-${PV}.tar.bz2"
S = "${WORKDIR}/xextproto-${PV}"

inherit native autotools pkgconfig

PROVIDES = "xextensions-native"
