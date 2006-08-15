SECTION = "x11/libs"
LICENSE= "MIT"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto"
PROVIDES = "xdmcp"
DESCRIPTION = "X Display Manager Control Protocol library."
PR = "r1"
S = "${WORKDIR}/libXdmcp-${PV}"

SRC_URI = "${XLIBS_MIRROR}/libXdmcp-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
