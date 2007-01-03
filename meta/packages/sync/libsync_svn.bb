LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade libopensync"
RRECOMMENDS = "\
	libopensync-plugin-file-sync \
	libopensync-plugin-syncml-client-plugin \
	libopensync-plugin-syncml-plugin \
	"
DESCRIPTION = "LibSync is a GObject-based framework for more convenient use of \
	OpenSync in GLib applications."
PV = "0.0+svn${SRCDATE}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=sync;proto=http"

inherit autotools pkgconfig

S = "${WORKDIR}/sync"

PACKAGES += "synctool"
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_synctool = "${bindir} ${datadir}"

