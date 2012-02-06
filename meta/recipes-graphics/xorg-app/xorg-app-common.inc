DESCRIPTION = "X application"
HOMEPAGE = "http://www.x.org/"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11/apps"
LICENSE = "MIT-X"
DEPENDS = "util-macros-native virtual/libx11"

INC_PR = "r7"

SRC_URI = "${XORG_MIRROR}/individual/app/${BPN}-${PV}.tar.bz2"

S = "${WORKDIR}/${BPN}-${PV}"

inherit autotools pkgconfig

FILES_${PN} += "  /usr/lib/X11/${BPN} /usr/share/X11/app-defaults/"
