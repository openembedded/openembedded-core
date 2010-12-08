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

SRC_URI[md5sum] = "0c3d078c2e8ea5a88971089a2f02a726"
SRC_URI[sha256sum] = "8b1b0da8f757b9ac318e1c15a0eac8bdb56ca902a2dd25beda06c0f265f22591"

inherit autotools

EXTRA_OECONF = "--enable-shared"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

BBCLASSEXTEND = "native"
