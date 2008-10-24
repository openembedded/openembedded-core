#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "X11 Pimlico Appications List"
PR = "r27"

PACKAGES = "\
    task-moblin-apps-x11-pimlico \
    task-moblin-apps-x11-pimlico-dbg \
    task-moblin-apps-x11-pimlico-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

WEB = "web-webkit"

RDEPENDS_task-moblin-apps-x11-pimlico = "\
    eds-dbus \
    contacts \
    dates \
    tasks \
    gaku \
    ${WEB}"
