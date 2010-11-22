LICENSE = "LGPL"
HOMEPAGE = "http://www.opensync.org/"
SUMMARY = "Synchronization framwork"
DESCRIPTION = "The OpenSync project is an ongoing effort to create a synchronization framework that will be a platform independent, general purpose synchronization engine utilizing modular plugins for content formats and different kind of connection types. OpenSync's modularity should allow it to be extended easily to new devices and purposes without radically changing the architecture itself, allowing it to support wide variety of devices used today and in the future."
DEPENDS = "sqlite3 libxml2 glib-2.0 libcheck zlib libxslt"

PR = "r1"

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

