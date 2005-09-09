DESCRIPTION = "GTK plugin for stylus based systems"
SECTION = "libs"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DEPENDS = "gtk+"
LICENSE = "LGPL"
FILES_${PN} = "/etc ${libdir}/gtk-2.0"

inherit autotools

SRC_URI = "http://burtonini.com/temp/${PN}-${PV}.tar.bz2 \
	file://makefile.patch;patch=1 \
	file://gtkstylus.sh"

do_install_append() {
	install -d ${D}/${sysconfdir}/X11/Xsession.d
	install -m 755 ${WORKDIR}/gtkstylus.sh ${D}/${sysconfdir}/X11/Xsession.d/45gtkstylus
}

