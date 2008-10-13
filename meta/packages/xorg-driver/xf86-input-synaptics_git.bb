require xf86-input-common.inc

DESCRIPTION = "X.Org X server -- keyboard input driver"

PV = "0.15.2+git${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/driver/xf86-input-synaptics;protocol=git"
S = "${WORKDIR}/git"

