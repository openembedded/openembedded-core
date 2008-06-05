#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "X11 Pimlico Appications List"
PR = "r27"

PACKAGES = "\
    task-poky-apps-x11-pimlico \
    task-poky-apps-x11-pimlico-dbg \
    task-poky-apps-x11-pimlico-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

WEB = "web-webkit"
# WebKit takes too much space to fit on some devices
# List here for now...
WEB_c7x0 = ""
WEB_mx31ads = ""

RDEPENDS_task-poky-apps-x11-pimlico = "\
    eds-dbus \
    contacts \
    dates \
    tasks \
    gaku \
    ${WEB}"
