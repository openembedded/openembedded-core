require libx11.inc
require libx11_git.inc

DESCRIPTION += " Support for XCB, UDC, XCMS and XLOCALE is disabled in \
this version."

SRC_URI += "file://X18NCMSstubs.diff \
            file://fix-disable-xlocale.diff \
            file://fix-utf8-wrong-define.patch"

DEPENDS += "bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
            libxdmcp xf86bigfontproto kbproto inputproto xproto-native"

SRCREV = "d23aad31338e7d869d878d5aa1b6b91d20287005"

EXTRA_OECONF += "--without-xcb --disable-udc --disable-xcms --disable-xlocale"
CFLAGS += "-D_GNU_SOURCE"
