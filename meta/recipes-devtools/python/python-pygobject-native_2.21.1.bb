require python-pygobject_${PV}.bb

DEPENDS = "python-native glib-2.0-native"
PR = "r0"

PARALLEL_MAKE = ""

inherit native

SRC_URI = "ftp://ftp.gnome.org/pub/GNOME/sources/pygobject/${MAJ_VER}/pygobject-${PV}.tar.bz2"

do_install_append() {
	install -d ${D}${bindir}
	install -m 0755 gobject/generate-constants ${D}${bindir}/gobject-generate-constants
}
