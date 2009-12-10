SECTION = "x11/base"
SRC_URI = "file://gtk-doc.m4"
LICENSE = "LGPL"
PR = "r4"

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native"

NATIVE_INSTALL_WORKS = "1"
do_install () {
	install -d ${D}${datadir}/aclocal/
	install -m 0644 ${WORKDIR}/gtk-doc.m4 ${D}${datadir}/aclocal/
}
