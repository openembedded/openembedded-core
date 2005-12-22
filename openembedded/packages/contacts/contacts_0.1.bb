LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Contacts is an address-book application."

SRC_URI = "svn://svn.o-hand.com/repos/${PN}/tags;module=release-0.1;proto=http \
	   file://stock_contact.png \
	   file://stock_person.png \
	   file://index.theme"

inherit autotools pkgconfig

S = "${WORKDIR}/release-0.1"

do_install_append () {
	install -d ${D}/${datadir}/icons/hicolor/48x48/stock/net
	install -d ${D}/${datadir}/icons/hicolor/48x48/stock/generic

	install -m 0644 ${WORKDIR}/stock_contact.png ${D}/${datadir}/icons/hicolor/48x48/stock/net
	install -m 0644 ${WORKDIR}/stock_person.png ${D}/${datadir}/icons/hicolor/48x48/stock/generic
	install -m 0644 ${WORKDIR}/index.theme ${D}/${datadir}/icons/hicolor
}

FILES_${PN} += "${datadir}/icons/hicolor/48x48/apps/oh-contacts.png \
		${datadir}/icons/hicolor/48x48/stock/*/*.png \
		${datadir}/icons/hicolor/index.theme"

pkg_postinst_${PN} () {
	gtk-update-icon-cache /usr/share/icons/hicolor
}

