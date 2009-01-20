require clutter-gst.inc

PV = "0.9.0+git${SRCREV}"
PR = "r0"

SRC_URI = "git://git.clutter-project.org/clutter-gtk.git;protocol=git"

S = "${WORKDIR}/git"

DEPENDS = "clutter"
