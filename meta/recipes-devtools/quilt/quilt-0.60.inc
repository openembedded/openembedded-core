DESCRIPTION = "Tool to work with series of patches."
HOMEPAGE = "http://savannah.nongnu.org/projects/quilt/"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.savannah.gnu.org/releases/quilt/quilt-${PV}.tar.gz \
        file://install.patch"

SRC_URI[md5sum] = "058a08a9b626bdec9ec8c519dc92158c"
SRC_URI[sha256sum] = "3d72a292e432beb9a73f9d0acfe3a77c9b4d7e42209919bb244e9958c7cfe64b"

inherit autotools

EXTRA_OECONF_darwin += "--without-date \
                        --without-getopt \
                        "

PACKAGES += "guards guards-doc"
FILES_${PN} = "${sysconfdir} ${datadir}/quilt \
               ${bindir}/quilt ${libdir}/quilt"
FILES_guards = "${bindir}/guards"
FILES_${PN}-doc = "${mandir}/man1/quilt.1 ${docdir}/${BPN}"
FILES_guards-doc = "${mandir}/man1/guards.1"

RDEPENDS_${PN} = "bash"

EXTRA_OE_MAKE_ARGS_darwin ?= ""
EXTRA_OE_MAKE_ARGS ?= "BUILD_ROOT=${D}"

# quilt ignores DESTDIR
do_install () {
	oe_runmake ${EXTRA_OE_MAKE_ARGS} install
	# cleanup unpackaged files
	rm -rf ${D}/${datadir}/emacs
}
