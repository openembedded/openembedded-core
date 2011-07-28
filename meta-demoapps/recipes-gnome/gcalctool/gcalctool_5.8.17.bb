LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "gtk+ gnome-doc-utils"
DESCRIPTION = "gcalctool is a powerful calculator"
PR = "r0"

SRC_URI = "http://download.gnome.org/sources/${BPN}/5.8/${BPN}-${PV}.tar.gz \
	file://fix-includedir.patch;patch=1"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-gnome"
