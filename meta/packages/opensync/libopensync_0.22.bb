LICENSE = "LGPL"
HOMEPAGE = "http://www.opensync.org/"

DEPENDS = "sqlite3 libxml2 zlib glib-2.0"

SRC_URI = "http://www.opensync.org/attachment/wiki/download/libopensync-0.22.tar.bz2?format=raw"

inherit autotools pkgconfig lib_package

EXTRA_OECONF = "--disable-python"
LEAD_SONAME = "libopensync.so"

FILES_${PN} += " ${libdir}/opensync/formats/*.so"
FILES_${PN}-dbg += " ${libdir}/opensync/formats/.debug/*.so"

require opensync-unpack.inc

do_stage() {
autotools_stage_all
}

