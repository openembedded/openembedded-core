require libx11.inc
inherit gettext

PR = "r3"

BBCLASSEXTEND = "native nativesdk"

EXTRA_OECONF += "--with-keysymdefdir=${STAGING_INCDIR}/X11"

DEPENDS += "util-macros xtrans libxdmcp libxau \
            bigreqsproto xproto xextproto xcmiscproto \
            xf86bigfontproto kbproto inputproto libxcb \
            xproto-native"

SRC_URI += " file://keysymdef_include.patch \
             file://x11_disable_makekeys.patch \
             file://0001-Add-_XGetRequest-as-substitute-for-GetReq-GetReqExtr.patch \
             file://makekeys_crosscompile.patch \
             "

SRC_URI[md5sum] = "ed7c382cbf8c13425b6a66bcac0ca5d9"
SRC_URI[sha256sum] = "7fe62180f08ef5f0a0062fb444591e349cae2ab5af6ad834599f5c654e6c840d"
