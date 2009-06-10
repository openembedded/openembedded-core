require python-pygobject_${PV}.bb

DEPENDS = "python-native glib-2.0-native"
PR = "r1"

PARALLEL_MAKE = ""

inherit native

SRC_URI = "\
  ftp://ftp.gnome.org/pub/GNOME/sources/pygobject/${MAJ_VER}/pygobject-${PV}.tar.bz2 \
#  file://python-path.patch;patch=1 \
"

do_stage_append() {
	install -d ${STAGING_BINDIR}
	install -m 0755 gobject/generate-constants ${STAGING_BINDIR}/gobject-generate-constants
}
