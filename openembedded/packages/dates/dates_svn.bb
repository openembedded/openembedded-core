LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Dates is a calendar application."

PR = "r5"

PV = "0.0+svn${SRCDATE}"
S = "${WORKDIR}/trunk"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"

inherit autotools pkgconfig

CFLAGS_prepend = " -DFRAMES=3 "
