PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT"
SECTION = "x11/libs"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "network API translation layer to \
insulate X applications and libraries from OS \
network vageries."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=xtrans"
S = "${WORKDIR}/xtrans"

inherit autotools  pkgconfig

do_stage() {
	autotools_stage_all
}
