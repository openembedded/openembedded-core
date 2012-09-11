require libx11.inc
inherit gettext

PR = "${INC_PR}.0"

BBCLASSEXTEND = "native nativesdk"

EXTRA_OECONF += "--with-keysymdefdir=${STAGING_INCDIR}/X11"

DEPENDS += "util-macros xtrans libxdmcp libxau \
            bigreqsproto xproto xextproto xcmiscproto \
            xf86bigfontproto kbproto inputproto libxcb \
            xproto-native"

SRC_URI += " file://keysymdef_include.patch \
             file://x11_disable_makekeys.patch \
             file://makekeys_crosscompile.patch \
             "

SRC_URI[md5sum] = "78b4b3bab4acbdf0abcfca30a8c70cc6"
SRC_URI[sha256sum] = "c382efd7e92bfc3cef39a4b7f1ecf2744ba4414a705e3bc1e697f75502bd4d86"
