#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
PR = "r1"

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
    clutter \
    clutter-cairo \
    clutter-gst"

RDEPENDS_task-poky-clutter-tests = "\
    clutter-examples \
    clutter-cairo-examples \
    clutter-gst-examples"

RDEPENDS_task-poky-clutter-apps = "\
    aaina \
    table"
