require clutter.inc

PR = "r1"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

SRC_URI = "http://source.clutter-project.org/sources/clutter/1.4/clutter-${PV}.tar.bz2 \
           file://enable_tests-1.4.patch;patch=1 \
           file://test-conformance-fix.patch;patch=1 "

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"
S = "${WORKDIR}/clutter-1.4.2"

BASE_CONF += "--disable-introspection"

EXTRA_OECONF += "--with-json=check"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}

SRC_URI[md5sum] = "5a3c6d8414d4e286aba0a936f344c9b1"
SRC_URI[sha256sum] = "92fd67acce5105c933e54ad0c87d0f5ace1202fd0f87949cb49a3759e6e38892"
