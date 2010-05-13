DESCRIPTION = "Zlib Compression Library"
SECTION = "libs"
PRIORITY = "required"
HOMEPAGE = "http://www.gzip.org/zlib/"
LICENSE = "zlib"
LIC_FILES_CHKSUM = "file://README;md5=ae764cfda68da96df20af9fbf9fe49bd \
			file://zlib.h;beginline=1;endline=30;md5=6ab03f03a5ee92d06b809797d4d5586d "
PR = "r7"

SRC_URI = "http://www.zlib.net/zlib-1.2.3.tar.bz2 \
		file://1.2.3.3.dfsg.patch.gz;patch=1 \
		file://visibility.patch;patch=1 \
		file://autotools.patch;patch=1 "

DEPENDS = "libtool-cross"

inherit autotools

BBCLASSEXTEND = "native nativesdk"
