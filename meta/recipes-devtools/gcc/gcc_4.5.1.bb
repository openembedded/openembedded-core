require gcc-${PV}.inc
require gcc-configure-target.inc
require gcc-package-target.inc

SRC_URI_append = "file://fortran-cross-compile-hack.patch"

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_INCDIR}"


SRC_URI[md5sum] = "48231a8e33ed6e058a341c53b819de1a"
SRC_URI[sha256sum] = "45fa81face89203ccbf3ec73f7a372769d855f3ba7446f50125b613d9c163e2c"
