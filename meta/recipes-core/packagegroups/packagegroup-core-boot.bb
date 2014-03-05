#
# Copyright (C) 2007 OpenedHand Ltd.
#

SUMMARY = "Minimal boot requirements"
DESCRIPTION = "The minimal set of packages required to boot the system"
LICENSE = "MIT"
PR = "r17"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

#
# Set by the machine configuration with packages essential for device bootup
#
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS ?= ""

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-boot"
RREPLACES_${PN} = "task-core-boot"
RCONFLICTS_${PN} = "task-core-boot"

# Distro can override the following VIRTUAL-RUNTIME providers:
VIRTUAL-RUNTIME_dev_manager ?= "udev"
VIRTUAL-RUNTIME_login_manager ?= "busybox"
VIRTUAL-RUNTIME_init_manager ?= "sysvinit"
VIRTUAL-RUNTIME_initscripts ?= "initscripts"
VIRTUAL-RUNTIME_keymaps ?= "keymaps"

SYSVINIT_SCRIPTS = "${@base_contains('MACHINE_FEATURES', 'rtc', 'busybox-hwclock', '', d)} \
                    modutils-initscripts \
                    init-ifupdown \
                   "

RDEPENDS_${PN} = "\
    base-files \
    base-passwd \
    busybox \
    ${@base_contains("DISTRO_FEATURES", "sysvinit", "${SYSVINIT_SCRIPTS}", "", d)} \
    ${@base_contains("MACHINE_FEATURES", "keyboard", "${VIRTUAL-RUNTIME_keymaps}", "", d)} \
    netbase \
    ${VIRTUAL-RUNTIME_login_manager} \
    ${VIRTUAL-RUNTIME_init_manager} \
    ${VIRTUAL-RUNTIME_initscripts} \
    ${VIRTUAL-RUNTIME_dev_manager} \
    ${VIRTUAL-RUNTIME_update-alternatives} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS}"

RRECOMMENDS_${PN} = "\
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"
