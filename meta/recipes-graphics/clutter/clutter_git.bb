require clutter.inc
require clutter-package.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

# the 1.8.2 tag
SRCREV = "9041ea42655dfc1422ce88eab931382dd400d13a"
PV = "1.8.2+git${SRCPV}"
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
