DESCRIPTION = "Address-book application."
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ eds-dbus"
DEPENDS_append_poky = " libowl"
RDEPENDS = "libedata-book"

inherit autotools_stage pkgconfig

OWL = "--disable-owl"
OWL_poky = "--enable-owl"

EXTRA_OECONF += "--disable-gnome-vfs ${OWL}"
EXTRA_OEMAKE += "GCONF_DISABLE_MAKEFILE_SCHEMA_INSTALL=1"

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_contact.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_person.png ${D}/${datadir}/pixmaps
}

FILES_${PN} += "${datadir}/pixmaps/stock_contact.png \
		${datadir}/pixmaps/stock_person.png"

SRC_URI = "file://stock_contact.png \
           file://stock_person.png"

SRC_URI_append_poky = " file://contacts-owl-window-menu.patch;patch=1 "
