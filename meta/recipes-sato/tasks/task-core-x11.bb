#
# Copyright (C) 2011 Intel Corporation
#

DESCRIPTION = "Tasks for core X11 applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r35"

PACKAGES = "\
    task-core-apps-x11-core \
    task-core-apps-x11-core-dbg \
    task-core-apps-x11-core-dev \
    task-core-apps-x11-games \
    task-core-apps-x11-games-dbg \
    task-core-apps-x11-games-dev \
    task-core-x11-base \
    task-core-x11-base-dbg \
    task-core-x11-base-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

XSERVER ?= "xserver-kdrive-fbdev"

ALLOW_EMPTY = "1"

# pcmanfm doesn't work on mips/powerpc
FILEMANAGER ?= "pcmanfm"
FILEMANAGER_mips ?= ""


RDEPENDS_task-core-x11-base = "\
    dbus \
    pointercal \
    matchbox-wm \
    matchbox-keyboard \
    matchbox-keyboard-applet \
    matchbox-keyboard-im \
    matchbox-panel-2 \
    matchbox-desktop \
    matchbox-session \
    ${XSERVER} \
    x11-common \
    xserver-nodm-init \
    liberation-fonts \
    xauth \
    xhost \
    xset \
    xrandr"


RDEPENDS_task-core-apps-x11-core = "\
    leafpad \
    ${FILEMANAGER} \
    matchbox-terminal \
    screenshot"


RDEPENDS_task-core-apps-x11-games = "\
    oh-puzzles"
