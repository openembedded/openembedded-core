SECTION = "x11/libs"
LICENSE = "MIT"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "network API translation layer to \
insulate X applications and libraries from OS \
network vageries."

SRC_URI = "${XLIBS_MIRROR}/libXtrans-0.1.tar.bz2"
S = "${WORKDIR}/libXtrans-${PV}"

inherit autotools  pkgconfig

do_stage() {
	autotools_stage_all
}
