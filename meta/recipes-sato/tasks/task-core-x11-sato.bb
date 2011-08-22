#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "Sato Tasks for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r31"

PACKAGES = "\
    task-core-x11-sato \
    task-core-x11-sato-dbg \
    task-core-x11-sato-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

NETWORK_MANAGER ?= "connman-gnome"
NETWORK_MANAGER_libc-uclibc = ""

RDEPENDS_task-core-x11-sato = "\
    matchbox-desktop \
    matchbox-session-sato \
    matchbox-keyboard \
    matchbox-stroke \
    matchbox-config-gtk \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    gthumb \
    x11vnc \
    libsdl \
    ${NETWORK_MANAGER}"
