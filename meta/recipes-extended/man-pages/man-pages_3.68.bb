SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://README;md5=8f2a3d43057d458e5066714980567a60"
SRC_URI = "${KERNELORG_MIRROR}/linux/docs/${BPN}/Archive/${BP}.tar.gz"

SRC_URI[md5sum] = "b23aa8262f9c999e66a413fb44a0a055"
SRC_URI[sha256sum] = "ad26df6a8878cb87309e8cb1e5867def3e3bfa731e3cf02af076db7d94f58f1f"

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
