SUMMARY = "Lossless data compression library"
HOMEPAGE = "http://www.oberhumer.com/opensource/lzo/"
SECTION = "libs"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://src/lzo_init.c;beginline=5;endline=25;md5=a6e25df9a83b24629e847846ccdd8054"

SRC_URI = "http://www.oberhumer.com/opensource/lzo/download/lzo-${PV}.tar.gz \
           file://0001-Use-memcpy-instead-of-reinventing-it.patch \
           file://acinclude.m4 \
           "

SRC_URI[md5sum] = "fcec64c26a0f4f4901468f360029678f"
SRC_URI[sha256sum] = "ac1b3e4dee46febe9fd28737eb7f5692d3232ef1a01da10444394c3d47536614"

inherit autotools

EXTRA_OECONF = "--enable-shared"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

BBCLASSEXTEND = "native nativesdk"
