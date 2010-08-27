DESCRIPTION = "Library for interacting with ID3 tags."
HOMEPAGE = "http://sourceforge.net/projects/mad/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=12349&atid=112349"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
			file://COPYRIGHT;md5=5e6279efb87c26c6e5e7a68317a6a87a \
			file://version.h;beginline=1;endline=8;md5=86ac68b67f054b7afde9e149bbc3fe63"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "zlib"
PR = "r3"

SRC_URI = "ftp://ftp.mars.org/pub/mpeg/libid3tag-${PV}.tar.gz \
           file://addpkgconfig.patch;patch=1"

S = "${WORKDIR}/libid3tag-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "-enable-speed"
