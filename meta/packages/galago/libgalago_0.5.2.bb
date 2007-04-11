DESCRIPTION =	"Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =	"http://www.galago-project.org/"
LICENSE =	"LGPL"
DEPENDS = 	"gettext dbus glib-2.0"

SRC_URI =	"http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz "

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-tests --disable-check"

do_stage() {
	autotools_stage_includes
	install -d ${STAGING_LIBDIR}
	install -m 755 libgalago/.libs/libgalago.so.3.0.0 ${STAGING_LIBDIR}/libgalago.so
}
