DESCRIPTION = "X application"
HOMEPAGE = "http://www.x.org/"
SECTION = "x11/apps"
LICENSE = "MIT-X"
DEPENDS = "util-macros-native virtual/libx11"

XORG_PN = "${PN}"

SRC_URI = "${XORG_MIRROR}/individual/app/${XORG_PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

do_stage() {
        autotools_stage_all
}

FILES_${PN} += "  /usr/lib/X11/${XORG_PN}"
