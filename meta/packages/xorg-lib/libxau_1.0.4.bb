require xorg-lib-common.inc

DESCRIPTION = "A Sample Authorization Protocol for X"
DEPENDS += " xproto"
PROVIDES = "xau"
PE = "1"

XORG_PN = "libXau"

BBCLASSEXTEND = "native nativesdk"
