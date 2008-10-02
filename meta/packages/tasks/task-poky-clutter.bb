#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
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
    clutter-0.8 \
    clutter-cairo-0.8 \
    clutter-gst-0.8 \
    clutter-gtk-0.8"

RDEPENDS_task-poky-clutter-tests = "\
    clutter-0.8-examples \
    clutter-cairo-0.8-examples \
    clutter-gst-0.8-examples \
    clutter-gtk-0.8-examples"

RDEPENDS_task-poky-clutter-apps = "\
    aaina \
    clutter-box2d \
    table"
