require pkgconfig.inc

SRC_URI += "file://autofoo.patch;patch=1"

DEPENDS += "glib-2.0"
EXTRA_OECONF = "--with-installed-glib"
