#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Gnome Mobile And Embedded Software Development Kit for OE-Core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r12"

inherit packagegroup

require packagegroup-sdk-gmae.inc

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} = "\
    packagegroup-core-sdk \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
