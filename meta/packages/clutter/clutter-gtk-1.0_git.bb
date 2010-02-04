require clutter-gtk.inc

PV = "0.90.0+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/clutter-gtk.git;protocol=git;branch=master"

S = "${WORKDIR}/git"

DEPENDS += "clutter-1.0"

EXTRA_OECONF += "--disable-introspection"
