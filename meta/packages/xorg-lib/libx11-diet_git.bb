require libx11.inc
require libx11_git.inc

SRC_URI += "file://X18NCMSstubs.diff;patch=1 \
            file://fix-disable-xlocale.diff;patch=1 \
            file://fix-utf8-wrong-define.patch;patch=1"

DEPENDS += "bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
            libxdmcp xf86bigfontproto kbproto inputproto xproto-native"

EXTRA_OECONF += "--without-xcb --disable-udc --disable-xcms --disable-xlocale"
CFLAGS += "-D_GNU_SOURCE"
