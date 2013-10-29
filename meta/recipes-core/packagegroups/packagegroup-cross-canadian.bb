SUMMARY = "Host SDK package for cross canadian toolchain"
PN = "packagegroup-cross-canadian-${MACHINE}"
LICENSE = "MIT"

# Save TRANSLATED_TARGET_ARCH before allarch tramples it
TRANSLATED_TARGET_ARCH = "${@d.getVar('TUNE_ARCH', True).replace('_', '-')}"

inherit cross-canadian packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} = "\
    binutils-cross-canadian-${@' binutils-cross-canadian-'.join(all_multilib_tune_values(d,'TRANSLATED_TARGET_ARCH').split())} \
    gdb-cross-canadian-${@' gdb-cross-canadian-'.join(all_multilib_tune_values(d, 'TRANSLATED_TARGET_ARCH').split())} \
    gcc-cross-canadian-${@' gcc-cross-canadian-'.join(all_multilib_tune_values(d, 'TRANSLATED_TARGET_ARCH').split())} \
    meta-environment-${MACHINE} \
    "

