SUMMARY = "Host SDK package for clang cross canadian toolchain"

include recipes-core/packagegroups/packagegroup-cross-canadian.bb

PN = "packagegroup-clang-cross-canadian-${MACHINE}"

CLANG = "clang-cross-canadian-${TRANSLATED_TARGET_ARCH}"

RDEPENDS:${PN} += "\
    ${@oe.utils.all_multilib_tune_values(d, 'CLANG')} \
"
