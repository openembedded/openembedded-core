#
# Copyright (C) 2011 Intel Corporation
#

SUMMARY = "X11 display server"
LICENSE = "MIT"
PR = "r40"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

XSERVER ?= "xserver-xorg xf86-video-fbdev xf86-input-evdev"
XSERVERCODECS ?= ""

RDEPENDS_${PN} = "\
    ${XSERVER} \
    ${XSERVERCODECS} \
    "

# Allow replacing task-x11-server in meta-oe
RPROVIDES_${PN} = "task-x11-server task-xserver"
RREPLACES_${PN} = "task-x11-server task-xserver"
RCONFLICTS_${PN} = "task-x11-server task-xserver"
