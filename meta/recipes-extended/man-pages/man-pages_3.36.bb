SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPLv2+"
PR = "r1"

LIC_FILES_CHKSUM = "file://README;md5=0422377a748010b2b738342e24f141c1"
SRC_URI = "${KERNELORG_MIRROR}/linux/docs/man-pages/Archive/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "cfe991b1ce9e0ea5355f29ff9fcac125"
SRC_URI[sha256sum] = "c0575e31f73cd9dc2761c274217e06e7130fdfee510170957a9d72d012a5437c"

RDEPENDS_${PN} = "man"

do_configure () {
	:
}

do_compile() {
	:
}

do_install() {
        oe_runmake install DESTDIR=${D}
}

FILES_${PN} += "${datadir}/man/"
