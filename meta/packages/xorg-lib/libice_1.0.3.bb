require xorg-lib-common.inc

PE = "1"

DESCRIPTION = "X11 ICE library"
PRIORITY = "optional"

DEPENDS += " xproto xtrans"
PROVIDES = "ice"

XORG_PN = "libICE"

