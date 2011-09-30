SUMMARY = "Zlib Compression Library"
DESCRIPTION = "Zlib is a general-purpose, patent-free, lossless data compression \
library which is used by many different programs."
HOMEPAGE = "http://zlib.net/"
SECTION = "libs"
LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://zlib.h;beginline=4;endline=23;md5=084e9c30e4e6272c3b057b13c6467f3d"

DEPENDS = "libtool-cross"
PR = "r1"

SRC_URI = "http://www.zlib.net/${BPN}-${PV}.tar.bz2 \
           file://configure.ac \
           file://Makefile.am \
	   file://fix.inverted.LFS.logic.patch"

SRC_URI[md5sum] = "be1e89810e66150f5b0327984d8625a0"
SRC_URI[sha256sum] = "239aead2f22f16bfcfa6a6a5150dcbd6d6f2e4d1eaa8727b5769ea014120b307"

inherit autotools

do_configure_prepend () {
	cp ${WORKDIR}/configure.ac ${S}/
	cp ${WORKDIR}/Makefile.am ${S}/
}

BBCLASSEXTEND = "native nativesdk"
