LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade gtkhtml2 curl gconf js"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Web is a multi-platform web browsing application."

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"
S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

