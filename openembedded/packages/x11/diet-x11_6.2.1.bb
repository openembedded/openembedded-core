SECTION = "x11/base"
include x11_${PV}.bb

EXTRA_OECONF = "--disable-xcms --disable-xlocale --disable-xkb"
CFLAGS += "-D_GNU_SOURCE"

SRC_URI += "file://fix-utf8-wrong-define.patch;patch=1"

