require xorg-util-common.inc
inherit native

#DESCRIPTION = ""

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/util/makedepend-${PV}.tar.bz2"
S="${WORKDIR}/makedepend-${PV}"

DEPENDS += " util-macros-native xproto-native"

