require xorg-lib-common.inc

DESCRIPTION = "X11 ICE library"
PRIORITY = "optional"

DEPENDS += " xproto xtrans"
PROVIDES = "ice"

XORG_PN = "libICE"

