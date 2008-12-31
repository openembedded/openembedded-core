#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Task for Moblin - minimal bootable image"
PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS = "virtual/kernel"
ALLOW_EMPTY = "1"
PR = "r8"

#
# Set by the machine configuration with packages essential for device bootup
#
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS ?= ""

RDEPENDS_task-moblin-boot = "\
    base-files \
    base-passwd \
    busybox \
    initscripts \
    ${@base_contains("MACHINE_FEATURES", "keyboard", "keymaps", "", d)} \
    modutils-initscripts \
    netbase \
    sysvinit \
    tinylogin \
    udev \
    ${VIRTUAL-RUNTIME_update-alternatives} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS}"

RDEPENDS_task-moblin-boot_append_netbook = "\
    sreadahead \
    sreadahead-generate \
    "

RDEPENDS_task-moblin-boot_append_menlow = "\
    sreadahead \
    sreadahead-generate \
    "

RRECOMMENDS_task-moblin-boot = "\
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"
