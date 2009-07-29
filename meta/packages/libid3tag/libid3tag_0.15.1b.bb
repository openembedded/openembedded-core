SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "zlib"
DESCRIPTION = "Library for interacting with ID3 tags."
LICENSE = "GPLv2"
PR = "r3"

SRC_URI = "ftp://ftp.mars.org/pub/mpeg/libid3tag-${PV}.tar.gz \
           file://addpkgconfig.patch;patch=1"

S = "${WORKDIR}/libid3tag-${PV}"

inherit autotools_stage pkgconfig

EXTRA_OECONF = "-enable-speed"
