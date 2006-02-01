LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade gtkhtml2 libsoup gconf js"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Web is a multi-platform web browsing application."

SRC_URI = "http://www.soton.ac.uk/~cil103/stuff/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig

