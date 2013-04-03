LICENSE = "GPLv2"
DEPENDS = "libxml-simple-perl-native"
PR = "r4"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://tango.freedesktop.org/releases/icon-naming-utils-${PV}.tar.gz"
SRC_URI[md5sum] = "2c5c7a418e5eb3268f65e21993277fba"
SRC_URI[sha256sum] = "044ab2199ed8c6a55ce36fd4fcd8b8021a5e21f5bab028c0a7cdcf52a5902e1c"

inherit autotools allarch perlnative

do_configure_append() {
	# Make sure we use our nativeperl wrapper.
	sed -i -e "1s:#!.*:#!/usr/bin/env nativeperl:" ${S}/icon-name-mapping.pl.in
}

FILES_${PN} += "${datadir}/dtds"

BBCLASSEXTEND = "native"
