#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Gnome Mobile And Embedded Software Development Kit for OpenedHand Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r12"

ALLOW_EMPTY = "1"

require task-sdk-gmae.inc

PACKAGES = "${PN} ${PN}-dbg"

RDEPENDS_${PN} = "\
    task-poky-standalone-sdk-target \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
