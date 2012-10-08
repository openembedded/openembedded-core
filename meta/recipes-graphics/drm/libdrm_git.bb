require libdrm.inc

SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"

SRCREV = "14db948127e549ea9234e02d8e112de3871f8f9f"
PV = "2.4.39+git${SRCPV}"
PR = "${INC_PR}.0"

