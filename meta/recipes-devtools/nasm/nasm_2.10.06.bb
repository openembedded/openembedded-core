DESCRIPTION = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"
COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'

PR = "r0"

SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2 "
SRC_URI[md5sum] = "875c4217b2e581dbae0bf96d45a6067a"
SRC_URI[sha256sum] = "ac84dd0d7e4ad6282061ce36b68a72db591db55cf0902b53d4297c743b0944a6"

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
