require recipes-graphics/clutter/clutter.inc

PR = "r2"

# Internal json-glib was removed in Clutter 1.5.2
STDDEPENDS += "json-glib"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

SRC_URI = "http://source.clutter-project.org/sources/clutter/1.6/clutter-${PV}.tar.bz2 \
           file://enable_tests-1.4.patch \
           file://update_gettext_macro_version.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"
S = "${WORKDIR}/clutter-${PV}"

BASE_CONF += "--disable-introspection"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}

SRC_URI[md5sum] = "1da9f983115f9bd28b0be8176e53fe36"
SRC_URI[sha256sum] = "0564e57ca8eb24e76014627c0bb28a80a6c01b620ba14bc4198365562549576d"
