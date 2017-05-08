SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPLv2+"

LIC_FILES_CHKSUM = "file://README;md5=8f2a3d43057d458e5066714980567a60"
SRC_URI = "${KERNELORG_MIRROR}/linux/docs/${BPN}/Archive/${BP}.tar.gz"

SRC_URI[md5sum] = "fa03cff75c7cee5a0fd5db3456aea43a"
SRC_URI[sha256sum] = "5995444d589a1ab83ae1220a4446ee09b3dadb6ce7037162ea75874ebcb2c040"

RDEPENDS_${PN} = "man"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
        oe_runmake install DESTDIR=${D}
}

# Only deliveres man-pages so FILES_${PN} gets everything
FILES_${PN}-doc = ""
FILES_${PN} = "${mandir}/*"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "passwd.5 getspnam.3"
ALTERNATIVE_LINK_NAME[passwd.5] = "${mandir}/man5/passwd.5"
ALTERNATIVE_LINK_NAME[getspnam.3] = "${mandir}/man3/getspnam.3"
