LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Dates is a calendar application."

PV = "0.0cvs${CVSDATE}"
S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/;module=${PN};proto=http"

inherit autotools pkgconfig

