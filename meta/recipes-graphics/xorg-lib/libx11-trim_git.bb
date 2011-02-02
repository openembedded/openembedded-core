require libx11.inc
require libx11_git.inc

DESCRIPTION += " Support for XCB, and XCMS is disabled in this version."

PR = "r2"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

EXTRA_OECONF += "--disable-xcms --with-xcb"
CFLAGS += "-D_GNU_SOURCE"

