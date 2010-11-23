SUMMARY =       "Desktop presence framework"
DESCRIPTION =   "Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =      "http://www.galago-project.org/"
LICENSE =       "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS =       "gettext libgalago dbus glib-2.0"

SRC_URI =       "http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz "

EXTRA_OECONF =	"--disable-binreloc --disable-check --disable-tests"

FILES_${PN} += "${datadir}/dbus-1/services/"

inherit autotools pkgconfig

