require clutter-gst.inc

PV = "0.8.0+git${SRCREV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git \
           file://autofoo.patch;patch=1"

S = "${WORKDIR}/git"
