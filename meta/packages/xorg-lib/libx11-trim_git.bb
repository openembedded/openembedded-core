require libx11.inc
require libx11_git.inc

PR = "r1"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

EXTRA_OECONF += "--disable-xcms --with-xcb"
CFLAGS += "-D_GNU_SOURCE"

