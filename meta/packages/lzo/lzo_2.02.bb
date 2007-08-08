DESCRIPTION = "Lossless data compression library"
HOMEPAGE = "http://www.oberhumer.com/opensource/lzo/"
LICENSE = "GPLv2"
SECTION = "libs"
PRIORITY = "optional"
PR = "r1"

SRC_URI = "http://www.oberhumer.com/opensource/lzo/download/lzo-${PV}.tar.gz \
           file://autofoo.patch;patch=1 \
           file://acinclude.m4"

inherit autotools 

EXTRA_OECONF = "--enable-shared"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

do_stage() {
	autotools_stage_all
}
