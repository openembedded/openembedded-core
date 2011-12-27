DESCRIPTION = "Tool to work with series of patches."
HOMEPAGE = "http://savannah.nongnu.org/projects/quilt/"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.savannah.gnu.org/releases/quilt/quilt-${PV}.tar.gz \
        file://install.patch"

SRC_URI[md5sum] = "ff5e1e041363c3af3c8fda4b968c9d1d"
SRC_URI[sha256sum] = "fa4579628a98cf94472b36869e74f5cd0cb057d6fd13c1872a53f01c45aba8c3"

inherit autotools

PACKAGES += "guards guards-doc"
FILES_${PN} = "${sysconfdir} ${datadir}/quilt \
               ${bindir}/quilt ${libdir}/quilt"
FILES_guards = "${bindir}/guards"
FILES_${PN}-doc = "${mandir}/man1/quilt.1 ${docdir}/${P}"
FILES_guards-doc = "${mandir}/man1/guards.1"

RDEPENDS_${PN} = "bash"

# quilt ignores DESTDIR
do_install () {
	oe_runmake 'BUILD_ROOT=${D}' install
	if [ "${BUILD_ARCH}" == "${HOST_ARCH}" ]; then
		# Dummy quiltrc file for patch.bbclass
		install -d ${D}${bindir}/
		touch ${D}${bindir}/quiltrc
	fi
	# cleanup unpackaged files
	rm -rf ${D}/${datadir}/emacs
}
