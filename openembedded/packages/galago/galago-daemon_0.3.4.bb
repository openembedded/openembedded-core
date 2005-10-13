DESCRIPTION =   "Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =      "http://www.galago-project.org/"
MAINTAINER =    "Koen Kooi <koen@handhelds.org>"
LICENSE =       "GPL"
DEPENDS =       "gettext libgalago dbus glib-2.0"


SRC_URI =       "http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz \
                 file://no-check.patch;patch=1"
EXTRA_OECONF =	"--disable-binreloc"

inherit autotools pkgconfig

