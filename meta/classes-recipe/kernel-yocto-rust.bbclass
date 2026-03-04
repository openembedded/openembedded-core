#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

RUST_DEBUG_REMAP = "--remap-path-prefix=${WORKDIR}=${TARGET_DBGSRC_DIR} \
                    --remap-path-prefix=${TMPDIR}/work-shared=${TARGET_DBGSRC_DIR} \
"
KRUSTFLAGS = " ${RUST_DEBUG_REMAP}"
EXTRA_OEMAKE:append = " KRUSTFLAGS='${KRUSTFLAGS}'"

RUST_KERNEL_TASK_DEPENDS ?=  "rust-native:do_populate_sysroot clang-native:do_populate_sysroot bindgen-cli-native:do_populate_sysroot"
do_kernel_configme[depends] += "${RUST_KERNEL_TASK_DEPENDS}"

do_kernel_configme:append () {
        oe_runmake -C ${S} O=${B} rustavailable
}
