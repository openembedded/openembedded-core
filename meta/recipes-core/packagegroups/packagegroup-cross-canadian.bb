SUMMARY = "Host SDK package for cross canadian toolchain"
PN = "packagegroup-cross-canadian-${TRANSLATED_TARGET_ARCH}"
PR = "r0"
LICENSE = "MIT"

# Save TRANSLATED_TARGET_ARCH before allarch tramples it
TRANSLATED_TARGET_ARCH = "${@d.getVar('TUNE_ARCH', True).replace('_', '-')}"

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

