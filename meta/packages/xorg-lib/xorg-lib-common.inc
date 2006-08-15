HOMEPAGE = "http://www.x.org"
SECTION = "x11/libs"
#MAINTAINER = ""
LICENSE = "MIT-X"

DEPENDS = "util-macros"
XORG_PN = "${PN}"
SRC_URI = "${XORG_MIRROR}/${@bb.data.getVar('PV', d, 1)[0:7]}/src/lib/${XORG_PN}-${PV}.tar.gz"
S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF="--enable-malloc0returnsnull"

do_stage() {
	autotools_stage_all
}
