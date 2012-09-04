#
# Copyright (C) 2011 Intel Corporation
#

LICENSE = "MIT"
PR = "r37"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES = "${PN} ${PN}-xserver ${PN}-utils"

XSERVER ?= "xserver-xorg xf86-video-fbdev xf86-input-evdev"

# xserver-common, x11-common
VIRTUAL-RUNTIME_xserver_common ?= "x11-common"

# elsa, xserver-nodm-init
VIRTUAL-RUNTIME_graphical_init_manager ?= "xserver-nodm-init"

SUMMARY = "X11 display server and basic utilities"
RDEPENDS_${PN} = "\
    ${PN}-xserver \
    ${PN}-utils \
    "

SUMMARY_${PN}-xserver = "X11 display server"
RDEPENDS_${PN}-xserver = "\
    ${XSERVER} \
    "

SUMMARY_${PN}-utils = "X11 basic utilities and init"
RDEPENDS_${PN}-utils = "\
    ${VIRTUAL-RUNTIME_xserver_common} \
    ${VIRTUAL-RUNTIME_graphical_init_manager} \
    xauth \
    xhost \
    xset \
    xrandr \
    "

# Allow replacing task-x11* in meta-oe
RPROVIDES_${PN} = "task-x11"
RREPLACES_${PN} = "task-x11"
RCONFLICTS_${PN} = "task-x11"
RPROVIDES_${PN}-xserver = "task-x11-server task-xserver"
RREPLACES_${PN}-xserver = "task-x11-server"
RCONFLICTS_${PN}-xserver = "task-x11-server"
RPROVIDES_${PN}-utils = "task-x11-utils"
RREPLACES_${PN}-utils = "task-x11-utils"
RCONFLICTS_${PN}-utils = "task-x11-utils"
