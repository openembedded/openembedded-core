SUMMARY = "A library for atomic integer operations"
DESCRIPTION = "A library for atomic integer operations"
HOMEPAGE = "http://www.hpl.hp.com/research/linux/atomic_ops/"
SECTION = "optional"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://doc/LICENSING.txt;md5=607073e04548eac7d1f763e480477bab \
		   "
PR = "r1"

SRC_URI = "http://www.hpl.hp.com/research/linux/atomic_ops/download/libatomic_ops-${PV}.tar.gz \
          "

SRC_URI[md5sum] = "890acdc83a7cd10e2e9536062d3741c8"
SRC_URI[sha256sum] = "c4ee6e0c304c6f13bcc32968453cdb54b2ec233d8bf4cfcf266ee09dc33b4eb5"

S = "${WORKDIR}/libatomic_ops-${PV}"

ALLOW_EMPTY_${PN} = "1"

ARM_INSTRUCTION_SET = "arm"

inherit autotools pkgconfig

do_install_append() {
	# those contain only docs, not necessary for now.
	install -m 0755 -d ${D}${docdir}
	mv ${D}${datadir}/libatomic_ops ${D}${docdir}/${BPN}
}

BBCLASSEXTEND = "native nativesdk"
