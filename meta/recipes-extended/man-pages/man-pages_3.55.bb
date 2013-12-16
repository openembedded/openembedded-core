SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://README;md5=0422377a748010b2b738342e24f141c1"
SRC_URI = "${KERNELORG_MIRROR}/linux/docs/${BPN}/Archive/${BP}.tar.gz"

SRC_URI[md5sum] = "509eb3c4273d7b328b125f028828277c"
SRC_URI[sha256sum] = "86213428527b4c7872d1a32021d76b91de5188e001d2183d725a952dc01bcac6"

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
