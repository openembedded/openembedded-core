require recipes-graphics/clutter/clutter.inc

PR = "r0"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

SRC_URI = "http://source.clutter-project.org/sources/clutter/1.6/clutter-${PV}.tar.bz2 \
           file://enable_tests-1.4.patch \
           file://test-conformance-fix.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"
S = "${WORKDIR}/clutter-1.6.8"

BASE_CONF += "--disable-introspection"

EXTRA_OECONF += "--with-json=check"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}

SRC_URI[md5sum] = "9eedac4216f709a9f144940d24bfbb3e"
SRC_URI[sha256sum] = "cc147b8e7e62ed4b9b8a83df3db9788cf37db0c83970ba876228433f32bda442"
