DEFAULT_PREFERENCE = "-1"

include libxcb.inc
PV = "1.1.90.1+gitr${SRCPV}"

DEPENDS += "libpthread-stubs xcb-proto-native"

SRC_URI = "git://anongit.freedesktop.org/git/xcb/libxcb;protocol=git"
S = "${WORKDIR}/git"

PACKAGES =+ "libxcb-xinerama"
