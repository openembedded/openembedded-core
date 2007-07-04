HOMEPAGE = "http://www.x.org"
SECTION = "x11/libs"
LICENSE = "MIT-X"
DEPENDS = "util-macros xorg-headers-native"

XORG_PN = "${PN}"

SRC_URI = "${XORG_MIRROR}/individual/lib/${XORG_PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-malloc0returnsnull"

do_stage() {
        autotools_stage_all
}
