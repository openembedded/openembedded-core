SECTION = "x11/base"
require libx11_${PV}.bb

EXTRA_OECONF = "--disable-xcms --disable-xlocale --disable-xkb"

SRC_URI += "file://fix-utf8-wrong-define.patch;patch=1"
