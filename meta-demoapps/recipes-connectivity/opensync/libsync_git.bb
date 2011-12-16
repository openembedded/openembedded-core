SUMMARY = "GObject-based sync library"
DESCRIPTION = "LibSync is a GObject-based framework for more convenient use of \
OpenSync in GLib applications."
LICENSE = "LGPLv2"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade libopensync avahi"
RRECOMMENDS_${PN} = "\
	libopensync-plugin-file \
	"
SRCREV = "3f375969d56028505db97cd25ef1679a167cfc59"
PV = "0.0+gitr${SRCPV}"
PR = "r2"

SRC_URI = "git://git.yoctoproject.org/sync;protocol=git"

inherit autotools pkgconfig

S = "${WORKDIR}/sync"

PACKAGES += "synctool"
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_synctool = "${bindir} ${datadir}"
