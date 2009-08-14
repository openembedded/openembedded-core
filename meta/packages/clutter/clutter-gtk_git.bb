require clutter-gtk.inc

PV = "0.9.0+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/clutter-gtk.git;protocol=git"

S = "${WORKDIR}/git"

DEPENDS = "clutter"

EXTRA_OECONF += "--disable-introspection"
