DEFAULT_PREFERENCE = "-1"

include libxcb.inc
PV = "1.1.90.1+git${SRCREV}"
PR = "r2"

DEPENDS += "libpthread-stubs"

SRC_URI = "git://anongit.freedesktop.org/git/xcb/libxcb;protocol=git"
S = "${WORKDIR}/git"

PACKAGES =+ "libxcb-xinerama"
