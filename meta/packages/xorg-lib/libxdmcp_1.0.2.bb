require xorg-lib-common.inc
PE = "1"

DESCRIPTION = "X Display Manager Control Protocol library."
PRIORITY = "optional"

DEPENDS += " xproto"
PROVIDES = "xdmcp"

XORG_PN = "libXdmcp"

