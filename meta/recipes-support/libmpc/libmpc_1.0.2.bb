require libmpc.inc

DEPENDS = "gmp mpfr"

LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=e6a600fd5e1d9cbde2d983680233ad02"
SRC_URI = "http://www.multiprecision.org/mpc/download/mpc-${PV}.tar.gz"

SRC_URI[md5sum] = "68fadff3358fb3e7976c7a398a0af4c3"
SRC_URI[sha256sum] = "b561f54d8a479cee3bc891ee52735f18ff86712ba30f036f8b8537bae380c488"

S = "${WORKDIR}/mpc-${PV}"
BBCLASSEXTEND = "native nativesdk"

