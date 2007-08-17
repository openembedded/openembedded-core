require xorg-util-common.inc
inherit native

DESCRIPTION = "create dependencies in makefiles"
DEPENDS = "xproto-native util-macros-native"
PR = "r2"
PE = "1"

XORG_PN = "makedepend"
