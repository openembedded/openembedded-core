PV = "0.0+cvs${SRCDATE}"
LICENSE= "MIT"
PR = "r1"
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto"
PROVIDES = "xau"
DESCRIPTION = "Authorization Protocol for X."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xau"
S = "${WORKDIR}/Xau"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
