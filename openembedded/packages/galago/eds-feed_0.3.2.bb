DESCRIPTION =   "Galago linkage to the Evolution Data Server."
HOMEPAGE =      "http://www.galago-project.org/"
LICENSE =       "GPL"
DEPENDS =       "gettext libgalago dbus glib-2.0 eds-dbus"

SRC_URI =       "http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz \
                 file://disable-bonobo.patch;patch=1"
#                 file://no-check.patch;patch=1"
#EXTRA_OECONF =	"--disable-binreloc"

FILES_${PN} += "${libdir}/galago/eds-feed"

inherit autotools pkgconfig

