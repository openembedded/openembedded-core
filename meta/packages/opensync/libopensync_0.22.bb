LICENSE = "LGPL"
HOMEPAGE = "http://www.opensync.org/"

DEPENDS = "sqlite3 libxml2 zlib glib-2.0"

SRC_URI = "http://www.opensync.org/download/releases/0.22/libopensync-0.22.tar.bz2"

inherit autotools_stage pkgconfig lib_package

EXTRA_OECONF = "--disable-python"
LEAD_SONAME = "libopensync.so"

FILES_${PN} += " ${libdir}/opensync/formats/*.so"
FILES_${PN}-dbg += " ${libdir}/opensync/formats/.debug/*.so"
