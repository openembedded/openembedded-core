require libx11.inc

DESCRIPTION += " Support for XCB, UDC, XCMS and XLOCALE is disabled in \
this version."

LIC_FILES_CHKSUM = "file://COPYING;md5=597df7e9217e89ccaeb749f48ce2aeb0"

PR = "r3"

SRC_URI += "file://x11_disable_makekeys.patch \
            file://include_fix.patch \
            file://X18NCMSstubs.diff \
            file://fix-disable-xlocale.diff \
            file://fix-utf8-wrong-define.patch \
            file://nodolt.patch"

DEPENDS += "bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
            libxdmcp xf86bigfontproto kbproto inputproto xproto-native"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11"

EXTRA_OECONF += "--without-xcb --disable-udc --disable-xcms --disable-xlocale"
CFLAGS += "-D_GNU_SOURCE"
