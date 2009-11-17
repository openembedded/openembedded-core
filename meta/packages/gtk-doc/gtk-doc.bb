SECTION = "x11/base"
SRC_URI = "file://gtk-doc.m4"
LICENSE = "LGPL"
PR = "r3"

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native"

do_install () {
	install -d ${D}${datadir}/aclocal/
	install -m 0644 ${WORKDIR}/gtk-doc.m4 ${D}${datadir}/aclocal/
}
