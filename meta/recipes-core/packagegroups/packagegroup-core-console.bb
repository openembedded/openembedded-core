#
# Copyright (C) 2011 Intel Corporation
#

DESCRIPTION = "Tasks for core console applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r35"

PACKAGES = "\
    packagegroup-core-apps-console \
    packagegroup-core-apps-console-dbg \
    packagegroup-core-apps-console-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

SPLASH ?= "psplash"

RDEPENDS_packagegroup-core-apps-console = "\
    avahi-daemon \
    dbus \
    portmap \
    ${SPLASH}"

