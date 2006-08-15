DESCRIPTION = "X autotools macros"
SECTION = "x11/libs"
LICENSE= "Xorg"
#MAINTAINER = ""

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/util/util-macros-${PV}.tar.bz2"
S = "${WORKDIR}/util-macros-${PV}"

inherit native autotools pkgconfig

