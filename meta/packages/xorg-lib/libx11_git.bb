require libx11.inc
require libx11_git.inc

DEPENDS = "xproto xextproto xcmiscproto xf86bigfontproto kbproto inputproto \
           bigreqsproto xtrans libxau libxcb libxdmcp util-macros"

DEFAULT_PREFERENCE = "-1"

BBCLASSEXTEND = "nativesdk"