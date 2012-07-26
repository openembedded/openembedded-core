#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r4"

inherit packagegroup

PACKAGES = "\
    packagegroup-core-clutter-core \
    packagegroup-core-clutter-apps \
    packagegroup-core-clutter-tests \
    "

RDEPENDS_packagegroup-core-clutter-core = "\
    clutter-1.8 \
    clutter-gst-1.8 \
    clutter-gtk-1.8 \
    "

#RDEPENDS_packagegroup-core-clutter-tests = "\
#    clutter-gst-1.8-examples \
#    clutter-gtk-1.8-examples"

#RDEPENDS_packagegroup-core-clutter-apps = "\
#    clutter-box2d "
