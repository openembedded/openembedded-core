SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
PRIORITY = "required"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPL"
PR = "r1"

LIC_FILES_CHKSUM = "file://README;md5=0422377a748010b2b738342e24f141c1"
SRC_URI = "http://www.kernel.org/pub/linux/docs/man-pages/Archive/${PN}-${PV}.tar.gz"

SRC_URI[md5sum] = "cc6c2d4d4eb364fd8da718c412db09d4"
SRC_URI[sha256sum] = "61ef2fc36421e72eb92d1f533576a439a2fcdae20269393f740dd98abd8be519"

RDEPENDS_${PN} = "man"

do_configure_prepend() {
	rm -rf not_installed
}

do_configure () {
      :
}

do_compile() {
	oe_runmake -f Makefile DESTDIR=${D} MANDIR=${D}${mandir}
}

fakeroot do_install() {
        oe_runmake install DESTDIR=${D}
}

FILES_${PN} += "${datadir}/man/"
