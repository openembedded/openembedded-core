DEFAULT_PREFERENCE = "-1"

include xcb-proto.inc
PV = "1.2+gitr${SRCPV}"
PR = "r1"

SRC_URI = "git://anongit.freedesktop.org/git/xcb/proto;protocol=git"
S = "${WORKDIR}/git"
