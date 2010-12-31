DESCRIPTION = "Lossless data compression library"
HOMEPAGE = "http://www.oberhumer.com/opensource/lzo/"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8cad52263e636e25377bc18420118101 \
                    file://src/lzo_init.c;beginline=5;endline=36;md5=454f781644dfe945e3634a9b699e91f3"
PR = "r0"

SRC_URI = "http://www.oberhumer.com/opensource/lzo/download/lzo-${PV}.tar.gz \
           file://autoconf.patch \
           file://acinclude.m4 \
           "

SRC_URI[md5sum] = "a383c7055a310e2a71b9ecd19cfea238"
SRC_URI[sha256sum] = "4ee3a040facf39561f13e4ef2ab99a886fd68251a35d612486ed0625cc6ab428"

inherit autotools

EXTRA_OECONF = "--enable-shared"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

BBCLASSEXTEND = "native"
