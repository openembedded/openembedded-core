DESCRIPTION = "X protocol headers"
HOMEPAGE = "http://www.x.org"
SECTION = "x11/libs"
LICENSE = "MIT-X"

#DEPENDS = ""
XORG_PN = "${PN}"
XORG_RELEASE = "${@["individual",bb.data.getVar('PV', d, 1)[0:7]+"/src"][bb.data.getVar('PV', d, 1)[0:4] == "X11R"]}"
SRC_URI = "${XORG_MIRROR}/${XORG_RELEASE}/proto/${XORG_PN}-${PV}.tar.bz2"
S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
