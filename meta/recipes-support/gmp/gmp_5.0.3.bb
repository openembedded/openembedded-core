require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"
PR = "r0"

SRC_URI_append += "file://gmp_bugfix.patch \
                   file://use-includedir.patch \
                   file://gmp_fix_for_x32.patch"

export CC_FOR_BUILD = "${BUILD_CC}"

SRC_URI[md5sum] = "8061f765cc86b9765921a0c800615804"
SRC_URI[sha256sum] = "dcafe9989c7f332b373e1f766af8e9cd790fc802fdec422a1910a6ef783480e3"
