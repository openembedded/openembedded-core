#
# Copyright (C) 2010  Intel Corporation. All rights reserved
#

DESCRIPTION = "Qt Tasks for Poky"
LICENSE = "MIT"
PR = "r0"

PACKAGES = "\
    task-poky-qt-demos \
    task-poky-qt-demos-dbg \
    task-poky-qt-demos-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-qt-demos = "\
    fotowall \
    quicky \
    "
