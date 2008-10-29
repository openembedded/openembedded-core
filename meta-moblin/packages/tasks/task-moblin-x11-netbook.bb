#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Netbook GUI Tasks for Moblin"
PR = "r0"

PACKAGES = "\
    task-moblin-x11-netbook \
    task-moblin-x11-netbook-dbg \
    task-moblin-x11-netbook-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

NETWORK_MANAGER ?= "networkmanager-applet"
RDEPENDS_task-moblin-x11-netbook = "\
    metacity-clutter \
    matchbox-desktop \
    matchbox-session-netbook \
    matchbox-config-gtk \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    ${NETWORK_MANAGER}"
