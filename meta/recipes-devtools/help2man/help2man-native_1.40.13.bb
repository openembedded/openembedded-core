DESCRIPTION = "Program to create simple man pages"
SECTION = "devel"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "autoconf-native automake-native"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz"
SRC_URI[md5sum] = "ecbc98f1f146e404e85d7bef520b34d8"
SRC_URI[sha256sum] = "15d3b6ebbac90f6d2a21480ba5e33c03b480a342ce498a84b1804f03d75358ba"

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
