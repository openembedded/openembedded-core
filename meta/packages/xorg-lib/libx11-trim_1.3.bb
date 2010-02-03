require libx11.inc

PR = "r6"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

SRC_URI += "file://x11_disable_makekeys.patch;patch=1 \
            file://include_fix.patch;patch=1 \
            file://nodolt.patch;patch=1"

EXTRA_OECONF += "--disable-xcms --with-xcb"
