require xorg-proto-common.inc

DESCRIPTION = "Touchscreen calibration protocol"

PV = "0.0+git${SRCPV}"
PR = "r2"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/proto/calibrateproto;protocol=git \
           file://fix.patch;patch=1"
S = "${WORKDIR}/git"
