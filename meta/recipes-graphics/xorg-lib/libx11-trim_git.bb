require libx11.inc
require libx11_git.inc

DESCRIPTION += " Support for XCB, and XCMS is disabled in this version."

PR = "r2"
SRCREV = "d23aad31338e7d869d878d5aa1b6b91d20287005"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

EXTRA_OECONF += "--with-keysymdef=${STAGING_INCDIR}/X11/keysymdef.h --disable-xcms --with-xcb"
CFLAGS += "-D_GNU_SOURCE"

