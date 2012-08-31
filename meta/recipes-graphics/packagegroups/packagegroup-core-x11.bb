#
# Copyright (C) 2011 Intel Corporation
#

LICENSE = "MIT"
PR = "r36"

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
