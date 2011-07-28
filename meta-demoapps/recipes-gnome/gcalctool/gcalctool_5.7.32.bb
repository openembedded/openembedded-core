LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "gtk+"
DESCRIPTION = "gcalctool is a powerful calculator"
PR = "r2"

SRC_URI = "http://download.gnome.org/sources/${BPN}/5.7/${BPN}-${PV}.tar.gz \
        file://makefile-fix.diff;patch=1\
	file://fix-includedir.patch;patch=1"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-gnome"
