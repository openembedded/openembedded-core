require clutter.inc
require clutter-package.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

SRCREV = "e957e277b8a4893ce8c99e94402036d42a8b3748"
PV = "1.0.0+git${SRCPV}"
PR = "r9"

SRC_URI = "git://git.gnome.org/clutter;protocol=git;branch=master \
           file://enable_tests-654c26a1301c9bc5f8e3e5e3b68af5eb1b2e0673.patch;rev=654c26a1301c9bc5f8e3e5e3b68af5eb1b2e0673 \
           file://enable_tests.patch;notrev=654c26a1301c9bc5f8e3e5e3b68af5eb1b2e0673 "
S = "${WORKDIR}/git"

BASE_CONF += "--disable-introspection"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
