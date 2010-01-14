require libx11.inc
require libx11_git.inc

PR = "r2"

DEPENDS = "xproto xextproto xcmiscproto xf86bigfontproto kbproto inputproto \
           bigreqsproto xtrans libxau libxcb libxdmcp util-macros gettext"

DEFAULT_PREFERENCE = "-1"

BBCLASSEXTEND = "nativesdk"