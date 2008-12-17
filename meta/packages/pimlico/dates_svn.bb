require dates.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.4.5+svnr${SRCREV}"
S = "${WORKDIR}/trunk"

SRC_URI = "svn://svn.gnome.org/svn/${PN};module=trunk;proto=http \
	   file://dates-owl-window-menu.patch;patch=1 \
	  "
