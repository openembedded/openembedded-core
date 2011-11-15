require libx11.inc

DESCRIPTION += " Support for XCB, UDC, XCMS and XLOCALE is disabled in \
this version."

LIC_FILES_CHKSUM = "file://COPYING;md5=172255dee66bb0151435b2d5d709fcf7"

PR = "r0"

SRC_URI += "file://x11_disable_makekeys.patch \
            file://X18NCMSstubs.diff \
            file://keysymdef_include.patch \
            file://fix-disable-xlocale.diff \
            file://fix-utf8-wrong-define.patch \
           "


SRC_URI[md5sum] = "ed7c382cbf8c13425b6a66bcac0ca5d9"
SRC_URI[sha256sum] = "7fe62180f08ef5f0a0062fb444591e349cae2ab5af6ad834599f5c654e6c840d"

DEPENDS += "bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
            libxdmcp xf86bigfontproto kbproto inputproto xproto-native"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11"

EXTRA_OECONF += "--without-xcb --disable-udc --disable-xcms --disable-xlocale --with-keysymdefdir=${STAGING_INCDIR}/X11"
CFLAGS += "-D_GNU_SOURCE"

