require xf86-input-common.inc

DESCRIPTION = "X.Org X server -- mouse input driver"

PV = "1.3.0+git${SRCPV}"
PR = "r2"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/driver/xf86-input-mouse;protocol=git \
           file://unbreak.patch;patch=1"
S = "${WORKDIR}/git"

