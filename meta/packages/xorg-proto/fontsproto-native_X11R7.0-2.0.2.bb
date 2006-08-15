DESCRIPTION = "X protocol headers"
SECTION = "x11/libs"
LICENSE= "MIT-X"
#MAINTAINER = ""

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/proto/fontsproto-${PV}.tar.bz2"
S = "${WORKDIR}/fontsproto-${PV}"

inherit native autotools pkgconfig
