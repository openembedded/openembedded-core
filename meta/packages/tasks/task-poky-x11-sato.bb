#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "Sato Tasks for Poky"
PR = "r29"

PACKAGES = "\
    task-poky-x11-sato \
    task-poky-x11-sato-dbg \
    task-poky-x11-sato-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

NETWORK_MANAGER ?= "networkmanager-applet"
RDEPENDS_task-poky-x11-sato = "\
    matchbox-desktop \
    matchbox-session-sato \
    matchbox-keyboard \
    matchbox-stroke \
    matchbox-config-gtk \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    ${NETWORK_MANAGER}"
