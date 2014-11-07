SUMMARY = "Program for creating simple man pages"
SECTION = "devel"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "autoconf-native automake-native"

SRC_URI = "${GNU_MIRROR}/${BPN}/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "a1b7fe49eddae8a2537ed74ee9ef11cb"
SRC_URI[sha256sum] = "1ae7f15f53b0cc55b070ae49df2ee5caa942c71529054e157599427bba3c5633"

inherit autotools native

EXTRA_OECONF = "--disable-nls"

# We don't want to reconfigure things as it would require 'perlnative' to be
# used.
do_configure() {
	oe_runconf
}

do_install_append () {
	# Make sure we use /usr/bin/env perl
	sed -i -e "1s:#!.*:#! /usr/bin/env perl:" ${D}${bindir}/help2man
}
