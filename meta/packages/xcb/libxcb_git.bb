DEFAULT_PREFERENCE = "-1"

include libxcb.inc
PV = "1.0+git"
PR = "r0"

DEPENDS += "libpthread-stubs"

SRC_URI = "git://anongit.freedesktop.org/git/xcb;protocol=git"
S = "${WORKDIR}/git/xcb"

PACKAGES =+ "libxcb-xinerama"
