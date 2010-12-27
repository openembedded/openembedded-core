SUMMARY = "GObject-based sync library"
DESCRIPTION = "LibSync is a GObject-based framework for more convenient use of \
OpenSync in GLib applications."
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade libopensync avahi"
RRECOMMENDS_${PN} = "\
	libopensync-plugin-file \
	"
PV = "0.0+svnr${SRCPV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/sync/trunk;module=sync;proto=http"

inherit autotools pkgconfig

S = "${WORKDIR}/sync"

PACKAGES += "synctool"
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_synctool = "${bindir} ${datadir}"
