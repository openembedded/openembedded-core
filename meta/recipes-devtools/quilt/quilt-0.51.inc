DESCRIPTION = "Tool to work with series of patches."
HOMEPAGE = "http://savannah.nongnu.org/projects/quilt/"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.savannah.gnu.org/releases/quilt/quilt-${PV}.tar.gz \
        file://install.patch"

SRC_URI[md5sum] = "d56d23d5212297d5777502e84dc0ee80"
SRC_URI[sha256sum] = "05441399d36399b3f26cb12e29c98a8ce4d1287d036957fa22408807860af614"

inherit autotools

PACKAGES += "guards guards-doc"
FILES_${PN} = "${sysconfdir} ${datadir}/quilt \
               ${bindir}/quilt ${libdir}/quilt"
FILES_guards = "${bindir}/guards"
FILES_${PN}-doc = "${mandir}/man1/quilt.1 ${docdir}/${BPN}"
FILES_guards-doc = "${mandir}/man1/guards.1"

RDEPENDS_${PN} = "bash"

# quilt ignores DESTDIR
do_install () {
	oe_runmake 'BUILD_ROOT=${D}' install
	# cleanup unpackaged files
	rm -rf ${D}/${datadir}/emacs
}
