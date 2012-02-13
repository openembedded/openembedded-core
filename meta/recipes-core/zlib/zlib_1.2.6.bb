SUMMARY = "Zlib Compression Library"
DESCRIPTION = "Zlib is a general-purpose, patent-free, lossless data compression \
library which is used by many different programs."
HOMEPAGE = "http://zlib.net/"
SECTION = "libs"
LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://zlib.h;beginline=4;endline=23;md5=94d1b5a40dadd127f3351471727e66a9"

PR = "r1"

SRC_URI = "http://www.zlib.net/${BPN}-${PV}.tar.bz2 \
           file://remove.ldconfig.call.patch \
           "
SRC_URI[md5sum] = "dc2cfa0d2313ca77224b4d932b2911e9"
SRC_URI[sha256sum] = "fa3e3e4881fa5810b8903f2c7e0dcd5a0a673535f0438021c4bbb5db1b918c8e"

do_configure (){
	./configure --prefix=${prefix} --shared --libdir=${libdir}
}

do_compile (){
	oe_runmake
}

do_install() {
	oe_runmake DESTDIR=${D} install
}

BBCLASSEXTEND = "native nativesdk"
