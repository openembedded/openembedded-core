#
# Copyright (C) 2011 Intel Corporation
#

DESCRIPTION = "Tasks for core X11 applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r36"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES = "${PN} ${PN}-xserver ${PN}-utils"

XSERVER ?= "xserver-xorg xf86-video-fbdev xf86-input-evdev"

# xserver-common, x11-common
VIRTUAL-RUNTIME_xserver_common ?= "x11-common"

# elsa, xserver-nodm-init
VIRTUAL-RUNTIME_graphical_init_manager ?= "xserver-nodm-init"

RDEPENDS_${PN} = "\
    ${PN}-xserver \
    ${PN}-utils \
    "

RDEPENDS_${PN}-xserver = "\
    ${XSERVER} \
    "

RDEPENDS_${PN}-utils = "\
    ${VIRTUAL-RUNTIME_xserver_common} \
    ${VIRTUAL-RUNTIME_graphical_init_manager} \
    xauth \
    xhost \
    xset \
    xrandr \
    "
