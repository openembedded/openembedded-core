#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

inherit module

DEPENDS += " rust-native"

RUST_DEBUG_REMAP = "--remap-path-prefix=${S}=${TARGET_DBGSRC_DIR} "
KRUSTFLAGS = " ${RUST_DEBUG_REMAP}"
EXTRA_OEMAKE:append = " KRUSTFLAGS='${KRUSTFLAGS}'"

python __anonymous() {
    features = (d.getVar('KERNEL_RUST_SUPPORT') or "").split()
    if "True" not in features:
        raise bb.parse.SkipRecipe(
            "Skipping rust-out-of-tree-module: KERNEL_RUST_SUPPORT is not enabled"
       )
}
