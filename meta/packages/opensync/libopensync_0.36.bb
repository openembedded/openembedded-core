LICENSE = "LGPL"
HOMEPAGE = "http://www.opensync.org/"

DEPENDS = "sqlite3 libxml2 glib-2.0 check"

SRC_URI = "http://opensync.org/download/releases/${PV}/libopensync-${PV}.tar.bz2\
           file://cmake.patch;patch=1 \
	   file://build-in-src.patch;patch=1 \
	   file://no-python-check.patch;patch=1"

inherit cmake pkgconfig

LEAD_SONAME = "libopensync.so"

FILES_${PN} += " ${libdir}/opensync*/formats/*.so \
                 ${libdir}/opensync*/osplugin \
                 ${datadir}/opensync*/schemas \
                 ${datadir}/opensync*/capabilities \
                 ${datadir}/opensync*/descriptions \
		 "
FILES_${PN}-dbg += " ${libdir}/opensync*/formats/.debug/*.so \
	             ${libdir}/opensync*/.debug/osplugin "

do_stage() {
    autotools_stage_all
}
