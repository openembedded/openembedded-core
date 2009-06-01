#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "X11 Pimlico Appications List"
PR = "r28"

PACKAGES = "\
    task-moblin-apps-x11-pimlico \
    task-moblin-apps-x11-pimlico-dbg \
    task-moblin-apps-x11-pimlico-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

RDEPENDS_task-moblin-apps-x11-pimlico = "\
    eds-dbus \
    contacts \
    dates \
    tasks \
    gaku"