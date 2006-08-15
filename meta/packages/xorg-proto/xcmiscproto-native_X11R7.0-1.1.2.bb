DESCRIPTION = "X protocol headers"
SECTION = "x11/libs"
LICENSE= "MIT-X"
#MAINTAINER = ""

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/proto/xcmiscproto-${PV}.tar.bz2"
S = "${WORKDIR}/xcmiscproto-${PV}"

inherit native autotools pkgconfig
