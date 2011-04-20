#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "X11 Pimlico Appications List"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r27"

PACKAGES = "\
    task-core-apps-x11-pimlico \
    task-core-apps-x11-pimlico-dbg \
    task-core-apps-x11-pimlico-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

WEB ?= ""
#WEB = "web-webkit"
# WebKit takes too much space to fit on some devices
# List here for now...
#WEB_c7x0 = ""
#WEB_mx31ads = ""

RDEPENDS_task-core-apps-x11-pimlico = "\
    eds-dbus \
    contacts \
    dates \
    tasks \
    gaku \
    ${WEB}"
