require libx11.inc
require libx11_git.inc

inherit gettext

SRCREV = "d23aad31338e7d869d878d5aa1b6b91d20287005"
PR = "r2"

DEPENDS = "xproto xextproto xcmiscproto xf86bigfontproto kbproto inputproto \
           bigreqsproto xtrans libxau libxcb libxdmcp util-macros"

DEFAULT_PREFERENCE = "-1"

BBCLASSEXTEND = "nativesdk"
