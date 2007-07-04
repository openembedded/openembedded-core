require xorg-lib-common.inc

DESCRIPTION = "X Printing Extension (Xprint) client library"
DEPENDS += "libxext libxau printproto"
PR = "r1"
PE = "1"

XORG_PN = "libXp"

CFLAGS_append += " -I ${S}/include/X11/XprintUtil -I ${S}/include/X11/extensions"
