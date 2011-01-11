DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPL"
DEPENDS = "gtk+ startup-notification"
SECTION = "x11/wm"
PR = "r2"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/matchbox-desktop/2.0/matchbox-desktop-${PV}.tar.bz2 \
	file://dso_linking_change_build_fix.patch"

EXTRA_OECONF = "--enable-startup-notification"

inherit autotools pkgconfig
