#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Tasks for Moblin"
PR = "r31"

PACKAGES = "\
    task-moblin-apps-console \
    task-moblin-apps-console-dbg \
    task-moblin-apps-console-dev \
    task-moblin-apps-x11-core \
    task-moblin-apps-x11-core-dbg \
    task-moblin-apps-x11-core-dev \
    task-moblin-apps-x11-games \
    task-moblin-apps-x11-games-dbg \
    task-moblin-apps-x11-games-dev \
    task-moblin-x11-base \
    task-moblin-x11-base-dbg \
    task-moblin-x11-base-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

XSERVER ?= "xserver-kdrive-fbdev"

ALLOW_EMPTY = "1"


RDEPENDS_task-moblin-apps-console = "\
    avahi-daemon \
    dropbear \
    dbus \
    portmap \
    psplash"


RDEPENDS_task-moblin-x11-base = "\
    dbus \
    pointercal \
    matchbox-wm \
    matchbox-keyboard \
    matchbox-keyboard-applet \
    matchbox-keyboard-im \
    matchbox-panel-2 \
    matchbox-desktop \
    matchbox-session \
    ${XSERVER} \
    x11-common \
    xserver-nodm-init \
    liberation-fonts \
    xauth \
    xhost \
    xset \
    xrandr"


RDEPENDS_task-moblin-apps-x11-core = "\
    leafpad \
    pcmanfm \
    matchbox-terminal \
    screenshot"


RDEPENDS_task-moblin-apps-x11-games = "\
    oh-puzzles"
