DESCRIPTION = "Zlib Compression Library"
HOMEPAGE = "http://zlib.net/"
SECTION = "libs"
PRIORITY = "required"
LICENSE = "zlib"
LIC_FILES_CHKSUM = "file://zlib.h;beginline=4;endline=23;md5=084e9c30e4e6272c3b057b13c6467f3d"

DEPENDS = "libtool-cross"
PR = "r0"

SRC_URI = "http://www.zlib.net/${BPN}-${PV}.tar.bz2 \
           file://configure.ac \
           file://Makefile.am"

inherit autotools

do_configure_prepend () {
	cp ${WORKDIR}/configure.ac ${S}/
	cp ${WORKDIR}/Makefile.am ${S}/
}

BBCLASSEXTEND = "native nativesdk"
