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
    sreadahead \
    sreadahead-generate \
    ${VIRTUAL-RUNTIME_update-alternatives} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS}"

RRECOMMENDS_task-moblin-boot = "\
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"
