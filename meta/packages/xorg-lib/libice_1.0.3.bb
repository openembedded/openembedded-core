require xorg-lib-common.inc

DESCRIPTION = "X11 Inter-Client Exchange library"
DEPENDS += "xproto xtrans"
PROVIDES = "ice"
PR = "r1"
PE = "1"

XORG_PN = "libICE"
