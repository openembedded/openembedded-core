#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r4"

PACKAGES = "\
    task-core-clutter-core \
    task-core-clutter-core-dbg \
    task-core-clutter-core-dev \
    task-core-clutter-apps \
    task-core-clutter-apps-dbg \
    task-core-clutter-apps-dev \
    task-core-clutter-tests \
    task-core-clutter-tests-dbg \
    task-core-clutter-tests-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-core-clutter-core = "\
    clutter-1.8 \
    clutter-gst-1.8 \
    clutter-gtk-1.8 \
    "

#RDEPENDS_task-core-clutter-tests = "\
#    clutter-gst-1.8-examples \
#    clutter-gtk-1.8-examples"

#RDEPENDS_task-core-clutter-apps = "\
#    clutter-box2d "
