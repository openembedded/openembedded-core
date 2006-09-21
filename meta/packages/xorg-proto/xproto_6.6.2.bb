SECTION = "x11/libs"
LICENSE= "MIT"
DESCRIPTION = "X protocol and ancillary headers."

SRC_URI = "${XLIBS_MIRROR}/xproto-${PV}.tar.gz"
S = "${WORKDIR}/xproto-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
