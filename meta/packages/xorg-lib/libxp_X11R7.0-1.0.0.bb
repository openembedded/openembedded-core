require xorg-lib-common.inc

DESCRIPTION = "X print extension library."

DEPENDS += " virtual/libx11 libxext xextproto libxau printproto"

XORG_PN = "libXp"

CFLAGS_append += " -I ${S}/include/X11/XprintUtil -I ${S}/include/X11/extensions"
EXTRA_OECONF="--enable-malloc0returnsnull"
