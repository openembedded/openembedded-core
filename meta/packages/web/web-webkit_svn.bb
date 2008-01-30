DESCRIPTION = "Multi-platform web browsing application."
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade webkit-gtk curl gconf js libowl"

PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/web/branches;module=webkit;proto=http"

S = "${WORKDIR}/webkit"

inherit autotools pkgconfig gconf

FILES_${PN} += "${datadir}/web2"
