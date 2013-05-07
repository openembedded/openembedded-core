require libmpc.inc

DEPENDS = "gmp mpfr"

LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=e6a600fd5e1d9cbde2d983680233ad02"
SRC_URI = "http://www.multiprecision.org/mpc/download/mpc-${PV}.tar.gz"

SRC_URI[md5sum] = "b32a2e1a3daa392372fbd586d1ed3679"
SRC_URI[sha256sum] = "ed5a815cfea525dc778df0cb37468b9c1b554aaf30d9328b1431ca705b7400ff"

S = "${WORKDIR}/mpc-${PV}"
BBCLASSEXTEND = "native nativesdk"

