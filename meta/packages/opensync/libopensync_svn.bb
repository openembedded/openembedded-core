LICENSE = "LGPL"
HOMEPAGE = "http://www.opensync.org/"
DEPENDS = "sqlite3 libxml2 zlib glib-2.0"
PV = "0.20+svn${SRCDATE}"
SRC_URI = "svn://svn.opensync.org;module=trunk;proto=http"

inherit autotools pkgconfig lib_package

S = "${WORKDIR}/trunk"
EXTRA_OECONF = "--disable-python"
LEAD_SONAME = "libopensync.so"

FILES_${PN} += " ${libdir}/opensync/formats/*.so"

do_stage() {
	autotools_stage_all
}

