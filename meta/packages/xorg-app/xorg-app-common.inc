DESCRIPTION = "X application"
HOMEPAGE = "http://www.x.org"
SECTION = "x11/applications"
LICENSE = "MIT-X"

DEPENDS = "util-macros"
XORG_PN = "${PN}"
XORG_RELEASE = "${@["individual",bb.data.getVar('PV', d, 1)[0:7]+"/src"][bb.data.getVar('PV', d, 1)[0:4] == "X11R"]}"
SRC_URI = "${XORG_MIRROR}/${XORG_RELEASE}/app/${XORG_PN}-${PV}.tar.bz2"
S = "${WORKDIR}/${XORG_PN}-${PV}"

FILES_${PN} += "  /usr/lib/X11/${XORG_PN}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
