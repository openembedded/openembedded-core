require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"
PR = "r0"

SRC_URI_append += "file://use-includedir.patch \
                   file://gmp_fix_for_x32.patch"

export CC_FOR_BUILD = "${BUILD_CC}"

SRC_URI[md5sum] = "50c3edcb7c9438e04377ee9a1a061b79"
SRC_URI[sha256sum] = "35d4aade3e4bdf0915c944599b10d23f108ffedf6c3188aeec52221c5cf9a06f"
