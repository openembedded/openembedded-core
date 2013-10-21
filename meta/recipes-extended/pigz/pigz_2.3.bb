require pigz.inc
LIC_FILES_CHKSUM = "file://pigz.c;beginline=7;endline=21;md5=a21d4075cb00ab4ca17fce5e7534ca95"

SRC_URI[md5sum] = "042e3322534f2c3d761736350cac303f"
SRC_URI[sha256sum] = "74bbd5962f9420549fc987ddd1ccda692ec2b29d2d612fbbe26edf3fa348ff21"

NATIVE_PACKAGE_PATH_SUFFIX = "/${PN}"

BBCLASSEXTEND = "native nativesdk"

