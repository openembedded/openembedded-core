SUMMARY = "A library for atomic integer operations"
DESCRIPTION = "A library for atomic integer operations"
HOMEPAGE = "http://www.hpl.hp.com/research/linux/atomic_ops/"
SECTION = "optional"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://doc/LICENSING.txt;md5=607073e04548eac7d1f763e480477bab \
		   "
PR = "r7"

SRC_URI = "http://www.hpl.hp.com/research/linux/atomic_ops/download/libatomic_ops-${PV}.tar.gz \
           file://fedora/libatomic_ops-1.2-ppclwzfix.patch \
           file://gentoo/libatomic_ops-1.2-mips.patch \
           file://doublefix.patch"

SRC_URI[md5sum] = "1b65e48271c81e3fa2d7a9a69bab7504"
SRC_URI[sha256sum] = "a3d8768aa8fd2f6ae79be2d756b3a6b48816b3889ae906be3d5ffb2de5a5c781"

S = "${WORKDIR}/libatomic_ops-${PV}"

ALLOW_EMPTY_${PN} = "1"

ARM_INSTRUCTION_SET = "arm"

inherit autotools pkgconfig

do_install_append() {
	mv ${D}${datadir}/libatomic_ops ${D}${datadir}/libatomic-ops || true
}
