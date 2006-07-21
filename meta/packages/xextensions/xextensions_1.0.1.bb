SECTION = "x11/libs"
LICENSE= "BSD-X"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DESCRIPTION = "various extension headers."

SRC_URI = "${XLIBS_MIRROR}/xextensions-${PV}.tar.bz2"
S = "${WORKDIR}/xextensions-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
