require xorg-lib-common.inc

DESCRIPTION = "X Display Manager Control Protocol library"
DEPENDS += "xproto"
PROVIDES = "xdmcp"
PR = "r2"
PE = "1"

DEPENDS += "gettext"

XORG_PN = "libXdmcp"

BBCLASSEXTEND = "native nativesdk"
