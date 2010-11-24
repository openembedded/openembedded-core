DESCRIPTION = "A library for atomic integer operations"
LICENSE = "GPLv2&MIT"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://doc/LICENSING.txt;md5=607073e04548eac7d1f763e480477bab \
		   "
PR = "r4"

SRC_URI = "http://www.hpl.hp.com/research/linux/atomic_ops/download/libatomic_ops-1.2.tar.gz \
           file://fedora/libatomic_ops-1.2-ppclwzfix.patch;patch=1 \
           file://doublefix.patch;patch=1"

S = "${WORKDIR}/libatomic_ops-${PV}"

ALLOW_EMPTY_${PN} = "1"

inherit autotools pkgconfig
