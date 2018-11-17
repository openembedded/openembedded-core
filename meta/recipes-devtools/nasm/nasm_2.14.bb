SUMMARY = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"

SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2"

SRC_URI[md5sum] = "238a240d3f869a52f8ac38ee3f8faafa"
SRC_URI[sha256sum] = "d43cfd27cad53d0c22a9bf9702e9dffcc7018a0df21d15b92c56d250d747c744"

inherit autotools-brokensep

do_configure_prepend () {
	if [ -f ${S}/aclocal.m4 ] && [ ! -f ${S}/acinclude.m4 ]; then
		mv ${S}/aclocal.m4 ${S}/acinclude.m4
	fi
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}

BBCLASSEXTEND = "native"

DEPENDS = "groff-native"
