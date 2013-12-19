SUMMARY = "X application"
HOMEPAGE = "http://www.x.org/"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11/apps"
LICENSE = "MIT-X"
DEPENDS = "util-macros-native virtual/libx11"

INC_PR = "r8"

SRC_URI = "${XORG_MIRROR}/individual/app/${BPN}-${PV}.tar.bz2"

S = "${WORKDIR}/${BPN}-${PV}"

inherit autotools pkgconfig

FILES_${PN} += " ${libdir}/X11/${BPN} ${datadir}/X11/app-defaults/"
