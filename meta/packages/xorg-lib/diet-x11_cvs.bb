SECTION = "x11/base"
require libx11_cvs.bb

EXTRA_OECONF = "--disable-xcms --disable-xlocale --disable-xkb"

SRC_URI += "file://fix-utf8-wrong-define.patch;patch=1 \
	file://xim.patch;patch=1"
