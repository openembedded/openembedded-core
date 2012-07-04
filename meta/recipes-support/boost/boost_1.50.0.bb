include boost.inc

LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

PR = "${INC_PR}.0"

SRC_URI += "file://arm-intrinsics.patch"
SRC_URI[md5sum] = "52dd00be775e689f55a987baebccc462"
SRC_URI[sha256sum] = "c9ace2b8c81fa6703d1d17c7e478de3bc51101c5adbdeb3f6cb72cf3045a8529"
