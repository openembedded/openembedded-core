#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
LICENSE = "MIT"
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
    clutter-1.0 \
    clutter-cairo-1.0 \
    clutter-gst-1.0 \
    clutter-gtk-1.0"

RDEPENDS_task-poky-clutter-tests = "\
    clutter-cairo-1.0-examples \
    clutter-gst-1.0-examples \
    clutter-gtk-1.0-examples"

RDEPENDS_task-poky-clutter-apps = "\
    clutter-box2d "
