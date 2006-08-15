HOMEPAGE = "http://www.x.org"
SECTION = "x11/utils"
#MAINTAINER = ""
LICENSE = "MIT-X"

#DEPENDS = ""
XORG_PN = "${PN}"
SRC_URI = "${XORG_MIRROR}/${@bb.data.getVar('PV', d, 1)[0:7]}/src/util/${XORG_PN}-${PV}.tar.gz"
S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
