DESCRIPTION = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "simplifiedBSD"
LIC_CHKSUM_FILES = "file://LICENSE;md5=d89d124974e487e5d64da6f1cd8acfbb"
COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/nasm/nasm-${PV}.tar.bz2 "

inherit autotools

do_configure_prepend () {
	mv aclocal.m4 acinclude.m4
}

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${mandir}/man1

	oe_runmake 'INSTALLROOT=${D}' install
}

BBCLASSEXTEND = "native"
NATIVE_INSTALL_WORKS = "1"
