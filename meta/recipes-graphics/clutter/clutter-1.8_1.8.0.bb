require recipes-graphics/clutter/clutter.inc

PR = "r2"

# Internal json-glib was removed in Clutter 1.5.2
STDDEPENDS += "json-glib cogl atk"

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

SRC_URI[md5sum] = "71454425366d9a49948cdd2af9eafc89"
SRC_URI[sha256sum] = "79cd42c9a208b1bd2e2a0866ad7eb047a00835beabb401e99737af846812119c"
