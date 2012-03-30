DESCRIPTION = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d89d124974e487e5d64da6f1cd8acfbb"
COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/nasm/nasm-${PV}.tar.bz2 "

SRC_URI[md5sum] = "d8934231e81874c29374ddef1fbdb1ed"
SRC_URI[sha256sum] = "ac70ee451c73d742c9ff830502e5f8b1f648b2abffa8fd00944243283ba8e87c"

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
