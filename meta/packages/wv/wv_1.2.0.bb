LICENSE = "GPLv2"

DEPENDS = ""

SRC_URI = "http://switch.dl.sourceforge.net/sourceforge/wvware/wv-${PV}.tar.gz"

PR = "r1"

inherit autotools pkgconfig

S = "${WORKDIR}/${PN}-${PV}"

EXTRA_OECONF = ""

do_stage () {
	autotools_stage_all
}
