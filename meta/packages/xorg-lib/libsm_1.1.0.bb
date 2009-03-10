require xorg-lib-common.inc

DESCRIPTION = "X11 Session management library"
DEPENDS += "libice xproto xtrans e2fsprogs"
PR = "r1"
PE = "1"

XORG_PN = "libSM"

BBCLASSEXTEND = "native"
