DESCRIPTION = "Programs for accessing Microsoft Word documents"
HOMEPAGE = "http://wvware.sourceforge.net/"
LICENSE = "GPLv2"
DEPENDS = "libgsf"
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/wvware/wv-${PV}.tar.gz"

inherit autotools pkgconfig

S = "${WORKDIR}/${PN}-${PV}"

EXTRA_OECONF = ""

do_stage () {
	autotools_stage_all
}
