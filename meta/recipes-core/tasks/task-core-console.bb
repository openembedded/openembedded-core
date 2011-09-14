#
# Copyright (C) 2011 Intel Corporation
#

DESCRIPTION = "Tasks for core console applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r35"

PACKAGES = "\
    task-core-apps-console \
    task-core-apps-console-dbg \
    task-core-apps-console-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

SPLASH ?= "psplash"

RDEPENDS_task-core-apps-console = "\
    avahi-daemon \
    dbus \
    portmap \
    ${SPLASH}"

