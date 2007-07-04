require xorg-proto-common.inc

DESCRIPTION = "Touchscreen calibration protocol"

PV = "0.0+git${SRCDATE}"
PR = "r1"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/proto/calibrateproto;protocol=git;tag=a1d5ef0c73fbef3e758c51b57ac69ba9567bae04 \
           file://coords.patch;patch=1"

S = "${WORKDIR}/git"
