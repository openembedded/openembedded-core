SUMMARY = "Matchbox Window Manager Desktop"
LICENSE = "GPLv2.0+"
DEPENDS = "gtk+ startup-notification"
SECTION = "x11/wm"
PR = "r2"

SRC_URI = "http://downloads.yoctoproject.org/releases/matchbox/matchbox-desktop/2.0/matchbox-desktop-${PV}.tar.bz2 \
	file://dso_linking_change_build_fix.patch"

SRC_URI[md5sum] = "b0a4a47130272e2adab4e9feb43a6c9c"
SRC_URI[sha256sum] = "be33ff0bc8a9b1eee6af360a5118e470e69a6292ecc9e154ec6f349fce5c5f75"

EXTRA_OECONF = "--enable-startup-notification"

inherit autotools pkgconfig
