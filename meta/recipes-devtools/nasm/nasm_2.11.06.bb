SUMMARY = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"
COMPATIBLE_HOST = '(x86_64|i.86).*-(linux|freebsd.*)'


SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2 "
SRC_URI[md5sum] = "9d93e8df5f5c005567f47ed6ca3a93d4"
SRC_URI[sha256sum] = "87b6d97d6981ca468f9dafda44cf1bb1ba122cec329d4d389af5db04fa0c3e6c"

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
