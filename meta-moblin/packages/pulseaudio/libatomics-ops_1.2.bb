DESCRIPTION = "A library for atomic integer operations"
LICENSE = "MIT"

PR = "r4"

SRC_URI = "http://www.hpl.hp.com/research/linux/atomic_ops/download/libatomic_ops-1.2.tar.gz \
           file://fedora/libatomic_ops-1.2-ppclwzfix.patch;patch=1 \
           file://doublefix.patch;patch=1"

S = "${WORKDIR}/libatomic_ops-${PV}"

ALLOW_EMPTY_${PN} = "1"

inherit autotools pkgconfig
