DESCRIPTION = "Fotowall is a creative tool that allows you to layout your photos or pictures \
in a personal way. You can add pictures, then resize, move, change colors, text, shadows, etc.."

HOMEPAGE = "http://www.enricoros.com/opensource/fotowall"
LICENSE = "GPLv2+"
SECTION = "x11/apps"

DEPENDS = "qt4-x11-free"
RDEPENDS = "qt4-x11-free"

SRC_URI = "http://qt-apps.org/CONTENT/content-files/71316-Fotowall-0.9.tar.bz2"

S = "${WORKDIR}/Fotowall-${PV}"

inherit qmake2 pkgconfig

do_install() {
	oe_runmake INSTALL_ROOT=${D} install
}
