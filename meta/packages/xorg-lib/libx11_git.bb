DEFAULT_PREFERENCE = "-1"

require libx11.inc

DEPENDS = "xproto xextproto xcmiscproto xf86bigfontproto kbproto inputproto \
           bigreqsproto xtrans libxau libxcb libxdmcp util-macros"
PV = "1.1.99.1+gitr${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libX11;protocol=git"
S = "${WORKDIR}/git"

