#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Task for OpenedHand Poky - minimal bootable image"
PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS = "virtual/kernel"
ALLOW_EMPTY = "1"
PR = "r3"

#
# Set by the machine configuration with packages essential for device bootup
#
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS ?= ""

RDEPENDS_task-poky-boot = "\
    base-files \
    base-passwd \
    busybox \
    initscripts \
    modutils-initscripts \
    netbase \
    sysvinit \
    tinylogin \
    udev \
    update-alternatives \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS}"

RRECOMMENDS_task-poky-boot = "\
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"
