require clutter-gst.inc

PV = "0.8.0+git${SRCREV}"

DEPENDS += "clutter-0.8"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git;branch=clutter-gst-0-8"

S = "${WORKDIR}/git"
