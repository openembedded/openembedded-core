LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade gtkhtml2 curl gconf js"
DESCRIPTION = "Web is a multi-platform web browsing application."

PV = "0.0+svn${SRCDATE}"
PR = "r1"
SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"
S = "${WORKDIR}/trunk"

inherit autotools pkgconfig gconf

