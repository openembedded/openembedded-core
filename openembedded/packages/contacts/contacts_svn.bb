LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus gnome-vfs"
RDEPENDS = "gnome-vfs-plugin-file"
RRECOMMENDS = "gnome-vfs-plugin-http"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Contacts is an address-book application."
PR = "r5"

PV = "0.1+svn${SRCDATE}"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
	   file://stock_contact.png \
	   file://stock_person.png"

inherit autotools pkgconfig

S = "${WORKDIR}/trunk"

EXTRA_OECONF = "--enable-gnome-vfs"

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_contact.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_person.png ${D}/${datadir}/pixmaps
}

FILES_${PN} += "${datadir}/pixmaps/stock_contact.png \
		${datadir}/pixmaps/stock_person.png"

