DESCRIPTION =	"Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =	"http://www.galago-project.org/"
MAINTAINER =	"Koen Kooi <koen@handhelds.org>"
LICENSE =	"LGPL"
DEPENDS = 	"gettext dbus glib-2.0"
PR =		"r1"


SRC_URI =	"http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz \
		 file://no-check.patch;patch=1"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_includes
	install -d ${STAGING_LIBDIR}
	install -m 755 libgalago/.libs/libgalago.so.1.0.0 ${STAGING_LIBDIR}/libgalago.so
}
