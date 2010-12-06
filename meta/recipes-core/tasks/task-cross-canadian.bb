DESCRIPTION = "Host SDK package for cross canadian toolchain" 
PR = "r0"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

inherit cross-canadian

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    binutils-cross-canadian-${TRANSLATED_TARGET_ARCH} \
    gdb-cross-canadian-${TRANSLATED_TARGET_ARCH} \
    gcc-cross-canadian-${TRANSLATED_TARGET_ARCH} \
    "

