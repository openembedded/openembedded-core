PV = "0.0+cvs${SRCDATE}"
LICENSE= "MIT"
PR = "r1"
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto"
PROVIDES = "xdmcp"
DESCRIPTION = "X Display Manager Control Protocol library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xdmcp"
S = "${WORKDIR}/Xdmcp"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
