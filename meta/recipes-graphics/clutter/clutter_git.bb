require clutter.inc
require clutter-package.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS += "cogl"

SRCREV = "39db46123ed6bbbc3e6ad359a64d4d344ca9e11b"
PV = "1.8.0+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.gnome.org/clutter;protocol=git;branch=master \
           file://enable_tests-654c26a1301c9bc5f8e3e5e3b68af5eb1b2e0673.patch;rev=654c26a1301c9bc5f8e3e5e3b68af5eb1b2e0673 \
           file://enable_tests.patch;notrev=654c26a1301c9bc5f8e3e5e3b68af5eb1b2e0673 "
S = "${WORKDIR}/git"

BASE_CONF += "--disable-introspection"

AUTOTOOLS_AUXDIR = "${S}/build"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
