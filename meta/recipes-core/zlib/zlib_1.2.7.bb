SUMMARY = "Zlib Compression Library"
DESCRIPTION = "Zlib is a general-purpose, patent-free, lossless data compression \
library which is used by many different programs."
HOMEPAGE = "http://zlib.net/"
SECTION = "libs"
LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://zlib.h;beginline=4;endline=23;md5=94d1b5a40dadd127f3351471727e66a9"

SRC_URI = "http://www.zlib.net/${BPN}-${PV}.tar.bz2 \
           file://remove.ldconfig.call.patch \
           "
SRC_URI[md5sum] = "2ab442d169156f34c379c968f3f482dd"
SRC_URI[sha256sum] = "49e2e9658dfb036900da6ea0267a737fa3c4eee6666776d378c79d52e9334934"

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
