DESCRIPTION = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"
COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'

PR = "r0"

SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2 "
SRC_URI[md5sum] = "af1cd6669f5e98148cd5ec42dee90f19"
SRC_URI[sha256sum] = "c056e2abc83816892e448f9e9e95a3d21e9e096f44341b9d4853f62a443bba82"

inherit autotools

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
