DESCRIPTION =   "Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =      "http://www.galago-project.org/"
LICENSE =       "GPL"
DEPENDS =       "gettext libgalago dbus glib-2.0"

SRC_URI =       "http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz "

EXTRA_OECONF =	"--disable-binreloc --disable-check --disable-tests"

FILES_${PN} += "${datadir}/dbus-1/services/"

inherit autotools pkgconfig

