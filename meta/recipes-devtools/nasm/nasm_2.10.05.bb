DESCRIPTION = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"
COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'

PR = "r0"

SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2 "
SRC_URI[md5sum] = "d3c50875fd923b782749f883294336ed"
SRC_URI[sha256sum] = "de5af263ce344d3a89711c61802e3ad8a4e14a61d539f521f7554cdbbe04ed0f"

inherit autotools

do_configure_prepend () {
	if [ -f aclocal.m4 ] && [ ! -f acinclude.m4 ]; then
		mv aclocal.m4 acinclude.m4
	fi
}

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${mandir}/man1

	oe_runmake 'INSTALLROOT=${D}' install
}

BBCLASSEXTEND = "native"

DEPENDS = "groff-native"
