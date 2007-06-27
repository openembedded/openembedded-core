#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Task for OpenedHand Poky - minimal bootable image"
PACKAGE_ARCH = "${MACHINE_ARCH}"
ALLOW_EMPTY = "1"
PR = "r1"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""

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
