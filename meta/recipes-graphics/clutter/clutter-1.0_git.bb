require clutter.inc

PV = "1.0+git${SRCPV}"
PR = "r3"

PACKAGES =+ "clutter-examples-1.0"
FILES_clutter-examples-1.0 = "${bindir}/test-* ${pkgdatadir}/redhand.png"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=clutter-1.0 \
           file://enable_tests-1.0.patch;patch=1 "

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"
S = "${WORKDIR}/git"

BASE_CONF += "--disable-introspection"


do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
