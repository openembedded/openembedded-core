#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r3"

PACKAGES = "\
    task-poky-clutter-core \
    task-poky-clutter-core-dbg \
    task-poky-clutter-core-dev \
    task-poky-clutter-apps \
    task-poky-clutter-apps-dbg \
    task-poky-clutter-apps-dev \
    task-poky-clutter-tests \
    task-poky-clutter-tests-dbg \
    task-poky-clutter-tests-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-clutter-core = "\
    clutter-1.4 \
    clutter-gst-1.0 \
    clutter-gtk-1.4"

RDEPENDS_task-poky-clutter-tests = "\
    clutter-gst-1.0-examples \
    clutter-gtk-1.4-examples"

#RDEPENDS_task-poky-clutter-apps = "\
#    clutter-box2d "
