SUMMARY = "Host SDK package for cross canadian toolchain"
PN = "packagegroup-cross-canadian-${TRANSLATED_TARGET_ARCH}"
PR = "r0"
LICENSE = "MIT"

inherit cross-canadian packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-cross-canadian-${TRANSLATED_TARGET_ARCH}"

RDEPENDS_${PN} = "\
    binutils-cross-canadian-${TRANSLATED_TARGET_ARCH} \
    gdb-cross-canadian-${TRANSLATED_TARGET_ARCH} \
    gcc-cross-canadian-${TRANSLATED_TARGET_ARCH} \
    meta-environment-${TRANSLATED_TARGET_ARCH} \
    "

