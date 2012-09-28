include boost.inc

LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

PR = "${INC_PR}.1"

SRC_URI += "file://arm-intrinsics.patch \
            file://hash_enums.patch \
           "
SRC_URI[md5sum] = "4b6bd483b692fd138aef84ed2c8eb679"
SRC_URI[sha256sum] = "fb2d2335a29ee7fe040a197292bfce982af84a645c81688a915c84c925b69696"
