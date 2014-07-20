SUMMARY = "A low level cryptographic library"
HOMEPAGE = "http://www.lysator.liu.se/~nisse/nettle/"
SECTION = "libs"
LICENSE = "LGPLv2.1 & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://serpent-decrypt.c;beginline=53;endline=67;md5=bcfd4745d53ca57f82907089898e390d \
                    file://serpent-set-key.c;beginline=56;endline=70;md5=bcfd4745d53ca57f82907089898e390d"

DEPENDS += "gmp"

SRC_URI = "http://www.lysator.liu.se/~nisse/archive/${BP}.tar.gz"

SRC_URI[md5sum] = "003d5147911317931dd453520eb234a5"
SRC_URI[sha256sum] = "bc71ebd43435537d767799e414fce88e521b7278d48c860651216e1fc6555b40"

EXTRA_OECONF = "--disable-openssl"

do_configure_prepend() {
       if [ ! -e ${S}/acinclude.m4 -a -e ${S}/aclocal.m4 ]; then
               cp ${S}/aclocal.m4 ${S}/acinclude.m4
       fi
}

inherit autotools

BBCLASSEXTEND = "native nativesdk"
