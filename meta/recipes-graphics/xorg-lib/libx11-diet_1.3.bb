require libx11.inc

DESCRIPTION += " Support for XCB, UDC, XCMS and XLOCALE is disabled in \
this version."

LIC_FILES_CHKSUM = "file://COPYING;md5=597df7e9217e89ccaeb749f48ce2aeb0"

PR = "r4"

SRC_URI += "file://x11_disable_makekeys.patch \
            file://include_fix.patch \
            file://X18NCMSstubs.diff \
            file://fix-disable-xlocale.diff \
            file://fix-utf8-wrong-define.patch \
            file://nodolt.patch"

SRC_URI[md5sum] = "0545089013213e90aac19b8f8045d32e"
SRC_URI[sha256sum] = "34656d022ff2f94430b534612821428fe15ade028d86a42907958167f2e497ac"

DEPENDS += "bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
            libxdmcp xf86bigfontproto kbproto inputproto xproto-native"


EXTRA_OECONF += "--with-keysymdef=${STAGING_INCDIR}/X11/keysymdef.h --without-xcb --disable-udc --disable-xcms --disable-xlocale"
CFLAGS += "-D_GNU_SOURCE"
