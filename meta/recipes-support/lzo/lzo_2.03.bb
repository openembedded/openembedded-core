DESCRIPTION = "Lossless data compression library"
HOMEPAGE = "http://www.oberhumer.com/opensource/lzo/"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8cad52263e636e25377bc18420118101 \
                    file://src/lzo_init.c;beginline=5;endline=33;md5=094776237523b2e93124cf09b9c76aef"
PR = "r0"

SRC_URI = "http://www.oberhumer.com/opensource/lzo/download/lzo-${PV}.tar.gz \
           file://autofoo.patch \
           file://acinclude.m4"

inherit autotools

EXTRA_OECONF = "--enable-shared"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

BBCLASSEXTEND = "native"
