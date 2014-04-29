SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://README;md5=0422377a748010b2b738342e24f141c1"
SRC_URI = "${KERNELORG_MIRROR}/linux/docs/${BPN}/Archive/${BP}.tar.gz"

SRC_URI[md5sum] = "8d273473a803cbe9f41b29b2294e5fda"
SRC_URI[sha256sum] = "3e936fb931239882ab2816a1497218b48ddc4834793a9779dfe91328c7587eb0"

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

# Only deliveres man-pages so FILES_${PN} gets everything
FILES_${PN}-doc = ""
FILES_${PN} = "${mandir}/*"
