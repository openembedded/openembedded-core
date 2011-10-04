LICENSE = "GPLv2"
DEPENDS = "libxml-simple-perl-native"
PR = "r4"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://tango.freedesktop.org/releases/icon-naming-utils-0.8.7.tar.gz"

SRC_URI[md5sum] = "4abe604721ce2ccb67f451aa7ceb44d6"
SRC_URI[sha256sum] = "1cb49ce6a04626939893a447da696f20003903d61bd80c6d74d29dd79ca340d2"

S = "${WORKDIR}/icon-naming-utils-${PV}"

inherit autotools native perlnative

do_configure_append() {
	# Make sure we use our nativeperl wrapper.
	sed -i -e "1s:#!.*:#!/usr/bin/env nativeperl:" ${S}/icon-name-mapping.pl.in
}
