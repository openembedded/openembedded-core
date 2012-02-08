require recipes-graphics/clutter/clutter.inc
require recipes-graphics/clutter/clutter-package.inc

# We're API/ABI compatible and this may make things easier for layers
PROVIDES += "clutter-1.6"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

SRC_URI = "http://source.clutter-project.org/sources/clutter/1.8/clutter-${PV}.tar.bz2 \
           file://enable_tests-1.4.patch \
           file://update_gettext_macro_version.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
# because we've namespaced PN to clutter-1.8
S = "${WORKDIR}/clutter-${PV}"

BASE_CONF += "--disable-introspection"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}

SRC_URI[md5sum] = "487f70f9b59e1328b47f1db4094ab662"
SRC_URI[sha256sum] = "0d567177facd6913ac9c894e230ae48933125f02354ef965bbbf0586f1f0df91"
