DEFAULT_PREFERENCE = "-1"

include xcb-proto.inc
PV = "1.2+gitr${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xcb/proto;protocol=git"
S = "${WORKDIR}/git"

DEPENDS_append_virtclass-native = " python-native"
BBCLASSEXTEND = "native"
