#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Gnome Mobile And Embedded Software Development Kit for OE-Core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r11"

ALLOW_EMPTY = "1"

require task-sdk-gmae.inc

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    task-core-sdk \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
