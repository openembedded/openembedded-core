DESCRIPTION = "A library for atomic integer operations"
LICENSE = "MIT"

SRC_URI = "http://www.hpl.hp.com/research/linux/atomic_ops/download/libatomic_ops-1.2.tar.gz"
S = "${WORKDIR}/libatomic_ops-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}	
