#
# Copyright (C) 2008 Intel.
#

DESCRIPTION = "Netbook Tasks for Poky"
PR = "r1"

PACKAGES = "\
    task-poky-x11-netbook \
    task-poky-x11-netbook-dbg \
    task-poky-x11-netbook-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

NETWORK_MANAGER ?= "networkmanager-applet"
RDEPENDS_task-poky-x11-netbook = "\
    mutter \
    matchbox-desktop \
    matchbox-session-netbook \
    matchbox-config-gtk \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    ${NETWORK_MANAGER}"
