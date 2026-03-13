#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

RUST_KERNEL_TASK_DEPENDS ?=  "rust-native:do_populate_sysroot clang-native:do_populate_sysroot bindgen-cli-native:do_populate_sysroot"
do_kernel_configme[depends] += "${RUST_KERNEL_TASK_DEPENDS}"

do_kernel_configme:append () {
        oe_runmake -C ${S} O=${B} rustavailable
}
