require dtc.inc

LIC_FILES_CHKSUM = "file://GPL;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://libfdt/libfdt.h;beginline=3;endline=52;md5=fb360963151f8ec2d6c06b055bcbb68c"

SRCREV = "73dca9ae0b9abe6924ba640164ecce9f8df69c5a"
PV = "1.3.0+git${SRCPV}"
PR = "${INC_PR}.0"

S = "${WORKDIR}/git"

SRC_URI_PATCH = " file://remove_space_opt.patch"
SRC_URI_PATCH_virtclass-native = ""
SRC_URI += "${SRC_URI_PATCH}"

BBCLASSEXTEND = "native"
