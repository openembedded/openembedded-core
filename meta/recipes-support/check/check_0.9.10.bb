DESCRIPTION = "Library to read the extended image information (EXIF) from JPEG pictures"
HOMEPAGE = "http://sourceforge.net/projects/check"
SECTION = "libs"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"


SRC_URI = "${SOURCEFORGE_MIRROR}/${BPN}/${PV}/${BP}.tar.gz \
           file://run-ptest \
           file://ptest.patch \
          "

SRC_URI[md5sum] = "6d10a8efb9a683467b92b3bce97aeb30"
SRC_URI[sha256sum] = "823819235753e94ae0bcab3c46cc209de166c32ff2f52cefe120597db4403e6d"

inherit autotools ptest

RDEPENDS_${PN} = "make"

BBCLASSEXTEND = "native"
