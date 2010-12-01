DESCRIPTION = "Host SDK package for cross canadian toolchain" 
PR = "r0"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

inherit cross-canadian

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    binutils-cross-canadian-${TARGET_ARCH} \
    gdb-cross-canadian-${TARGET_ARCH} \
    gcc-cross-canadian-${TARGET_ARCH} \
    "

