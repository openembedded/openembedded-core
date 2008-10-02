require clutter-gtk.inc

PV = "0.6.0+svnr${SRCREV}"

DEPENDS += "clutter-0.6"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/branches;module=clutter-gtk-0-6;proto=http"


S = "${WORKDIR}/clutter-gtk-0-6"
