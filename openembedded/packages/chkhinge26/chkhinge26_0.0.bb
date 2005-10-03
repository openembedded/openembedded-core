LICENSE = "LGPL"
SECTION = "x11"
#DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
MAINTAINER = "Matthew Allum <mallum@openedhand.com>"
DESCRIPTION = "Chkhinge26 fires off cmds on cXXXX Zs."
PR = "r0"

SRC_URI = "http://butterfeet.org/misc/${PN}-${PV}.tar.gz \
	   file://hinge-handler"

inherit autotools pkgconfig

do_install_append () {
	install -m 0755 ${WORKDIR}/hinge-handler ${D}/${BINDIR}/
}

FILES_${PN} += "${BINDIR}/hinge-handler"

