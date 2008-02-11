require xorg-proto-common.inc

DESCRIPTION = "Touchscreen calibration protocol"

PV = "0.0+git${SRCREV}"
PR = "r1"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/proto/calibrateproto;protocol=git"
S = "${WORKDIR}/git"
