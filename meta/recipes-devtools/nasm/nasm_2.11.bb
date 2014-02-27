SUMMARY = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"
COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'


SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2 "
SRC_URI[md5sum] = "4cd558047ea5ed51fc2c7c94e249c7b8"
SRC_URI[sha256sum] = "1ce7e897c67255a195367a60c739a90a0b33a4a73f058f7cda3253bcf975642b"

inherit autotools-brokensep

do_configure_prepend () {
	if [ -f ${S}/aclocal.m4 ] && [ ! -f ${S}/acinclude.m4 ]; then
		mv ${S}/aclocal.m4 ${S}/acinclude.m4
	fi
}

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${mandir}/man1

	oe_runmake 'INSTALLROOT=${D}' install
}

BBCLASSEXTEND = "native"

DEPENDS = "groff-native"
