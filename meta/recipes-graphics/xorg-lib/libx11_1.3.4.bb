require libx11.inc

LICENSE = "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=bf75bfe4d05068311b5e6862d4b5f2c5"

PE = "1"
PR = "r0"

SRC_URI += "file://x11_disable_makekeys.patch \
            file://nodolt.patch \
            file://include_fix.patch"

DEPENDS += "bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
            libxdmcp xf86bigfontproto kbproto inputproto xproto-native gettext"

EXTRA_OECONF += "--without-xcb"

BBCLASSEXTEND = "native nativesdk"
