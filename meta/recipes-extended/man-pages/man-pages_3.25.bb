SUMMARY = "Linux man-pages"
DESCRIPTION = "The Linux man-pages project documents the Linux kernel and C library interfaces that are employed by user programs"
SECTION = "console/utils"
PRIORITY = "required"
HOMEPAGE = "http://www.kernel.org/pub/linux/docs/man-pages"
LICENSE = "GPL"
PR = "r0"

LIC_FILES_CHKSUM = "file://README;md5=9dab010c5baa416669e5d17381799dd5"
SRC_URI = "http://www.kernel.org/pub/linux/docs/man-pages/Archive/${PN}-${PV}.tar.gz"

SRC_URI[md5sum] = "98ec2324abea77d7a992470dd44d140c"
SRC_URI[sha256sum] = "474242842a78dc2121687b7db6c32b6c78af5458f2bd809e23563dd185e2b169"

RDEPENDS = "man"

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
