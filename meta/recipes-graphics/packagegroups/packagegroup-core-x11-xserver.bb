#
# Copyright (C) 2011 Intel Corporation
#

SUMMARY = "X11 display server"
LICENSE = "MIT"
PR = "r40"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

XSERVER ?= "xserver-xorg xf86-video-fbdev xf86-input-evdev"
XSERVERCODECS ?= ""

RDEPENDS_${PN} = "\
    ${XSERVER} \
    ${XSERVERCODECS} \
    "
