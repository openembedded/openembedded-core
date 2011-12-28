include boost.inc

LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

PR = "${INC_PR}.0"

SRC_URI += "file://arm-intrinsics.patch"

SRC_URI[md5sum] = "d1e9a7a7f532bb031a3c175d86688d95"
SRC_URI[sha256sum] = "1bf254b2d69393ccd57a3cdd30a2f80318a005de8883a0792ed2f5e2598e5ada"
