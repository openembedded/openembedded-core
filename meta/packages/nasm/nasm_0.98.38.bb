DESCRIPTION = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "GPL"
COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

SRC_URI = "${SOURCEFORGE_MIRROR}/nasm/nasm-${PV}.tar.bz2"

inherit autotools

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${mandir}/man1

	oe_runmake 'INSTALLROOT=${D}' install
}
