SECTION = "x11/libs"
LICENSE= "MIT"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "X protocol and ancillary headers."

SRC_URI = "${XLIBS_MIRROR}/xproto-${PV}.tar.gz"
S = "${WORKDIR}/xproto-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
