DESCRIPTION = "A simple note-taking application with Wiki-style syntax and behaviour"
HOMEPAGE = "http://qt-apps.org/content/show.php/Quicky?content=80325"
LICENSE = "GPLv2+"
SECTION = "x11/apps"

DEPENDS = "qt4-x11-free"
RDEPENDS = "qt4-x11-free"

SRC_URI = "http://qt-apps.org/CONTENT/content-files/80325-quicky-0.4.tar.gz"

inherit qmake2 pkgconfig

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${S}/${PN} ${D}${bindir}
}
