require clutter-gtk.inc

PV = "0.10.3+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/clutter-gtk.git;protocol=git;branch=clutter-gtk-0.10"

S = "${WORKDIR}/git"

DEPENDS += "clutter-1.0"

EXTRA_OECONF += "--disable-introspection"
