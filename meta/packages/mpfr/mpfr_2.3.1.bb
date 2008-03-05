require mpfr.inc

DEPENDS = "gmp"
PR = "r1"

SRC_URI = "http://www.mpfr.org/mpfr-${PV}/mpfr-${PV}.tar.bz2"
S = "${WORKDIR}/mpfr-${PV}"

do_stage() {
	autotools_stage_all
}
