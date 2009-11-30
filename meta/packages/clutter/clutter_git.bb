require clutter.inc
require clutter-package.inc

PV = "1.0.0+git${SRCPV}"
PR = "r6"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=master \
           file://enable_tests.patch;patch=1 "
S = "${WORKDIR}/git"

BASE_CONF += "--disable-introspection"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}