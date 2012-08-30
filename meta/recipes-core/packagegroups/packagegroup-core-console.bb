#
# Copyright (C) 2011 Intel Corporation
#

DESCRIPTION = "Tasks for core console applications"
LICENSE = "MIT"
PR = "r35"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

SPLASH ?= "psplash"

RDEPENDS_packagegroup-core-apps-console = "\
    avahi-daemon \
    dbus \
    portmap \
    ${SPLASH}"

