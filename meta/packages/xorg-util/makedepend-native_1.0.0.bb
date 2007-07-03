require xorg-util-common.inc
inherit native

#DESCRIPTION = ""

XORG_PN = "makedepend"
S = "${WORKDIR}/makedepend-${PV}"

DEPENDS += " util-macros-native xproto-native"

PE = "1"
