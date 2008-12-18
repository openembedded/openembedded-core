require clutter-gst.inc

PV = "0.6.0+git${SRCREV}"

DEPENDS += "clutter-0.6"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git;branch=clutter-gst-0-6"

S = "${WORKDIR}/git"
