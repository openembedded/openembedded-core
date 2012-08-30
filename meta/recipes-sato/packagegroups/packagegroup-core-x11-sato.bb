#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "Sato Tasks for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r32"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES = "${PN} ${PN}-base ${PN}-apps ${PN}-games"

RDEPENDS_${PN} = "\
    ${PN}-base \
    ${PN}-apps \
    ${PN}-games \
    "

NETWORK_MANAGER ?= "connman-gnome"
NETWORK_MANAGER_libc-uclibc = ""

RDEPENDS_${PN}-base = "\
    matchbox-desktop \
    matchbox-session-sato \
    matchbox-keyboard \
    matchbox-keyboard-applet \
    matchbox-keyboard-im \
    matchbox-stroke \
    matchbox-config-gtk \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    libsdl \
    ${NETWORK_MANAGER} \
    "

# pcmanfm doesn't work on mips
FILEMANAGER ?= "pcmanfm"
FILEMANAGER_mips ?= ""

WEB ?= ""
#WEB = "web-webkit"

RDEPENDS_${PN}-apps = "\
    gthumb \
    leafpad \
    gaku \
    x11vnc \
    matchbox-terminal \
    sato-screenshot \
    ${FILEMANAGER} \
    ${WEB} \
    "

RDEPENDS_${PN}-games = "\
    oh-puzzles \
    "
