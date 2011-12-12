require recipes-graphics/clutter/clutter.inc
require recipes-graphics/clutter/clutter-package.inc

PR = "r3"

# We're API/ABI compatible and this may make things easier for layers
PROVIDES += "clutter-1.6"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

SRC_URI = "http://source.clutter-project.org/sources/clutter/1.8/clutter-${PV}.tar.bz2 \
           file://enable_tests-1.4.patch \
           file://update_gettext_macro_version.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
S = "${WORKDIR}/clutter-${PV}"

BASE_CONF += "--disable-introspection"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}

SRC_URI[md5sum] = "29a238fa11da2f56c40de0eb04424aaf"
SRC_URI[sha256sum] = "5e6abf6440e6be8130fd7a6b449e1789e4318f61b17f06323ba1b58dc143bc8b"
