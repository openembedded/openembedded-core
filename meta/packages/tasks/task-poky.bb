#
# Copyright (C) 2007-2008 OpenedHand Ltd.
#

DESCRIPTION = "Tasks for OpenedHand Poky"
PR = "r26"

PACKAGES = "\
    task-poky-apps-console \
    task-poky-apps-console-dbg \
    task-poky-apps-console-dev \
    task-poky-apps-x11-core \
    task-poky-apps-x11-core-dbg \
    task-poky-apps-x11-core-dev \
    task-poky-apps-x11-games \
    task-poky-apps-x11-games-dbg \
    task-poky-apps-x11-games-dev \
    task-poky-apps-x11-pimlico \
    task-poky-apps-x11-pimlico-dbg \
    task-poky-apps-x11-pimlico-dev \
    task-poky-x11-base \
    task-poky-x11-base-dbg \
    task-poky-x11-base-dev \
    task-poky-x11-sato \
    task-poky-x11-sato-dbg \
    task-poky-x11-sato-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

XSERVER ?= "xserver-kdrive-fbdev"

ALLOW_EMPTY = "1"


RDEPENDS_task-poky-apps-console = "\
    avahi-daemon \
    dropbear \
    portmap \
    psplash"


RDEPENDS_task-poky-x11-base = "\
    pointercal \
    matchbox-wm \
    matchbox-keyboard \
    matchbox-keyboard-applet \
    matchbox-keyboard-im \
    matchbox-panel-2 \
    matchbox-desktop \
    matchbox-session \
    ${XSERVER} \
    xserver-kdrive-common \
    xserver-nodm-init \
    liberation-fonts \
    xauth \
    xhost \
    xset \
    xrandr"


RDEPENDS_task-poky-apps-x11-core = "\
    leafpad \
    pcmanfm \
    matchbox-terminal \
    screenshot"


RDEPENDS_task-poky-apps-x11-games = "\
    oh-puzzles"

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

NETWORK_MANAGER = "networkmanager-applet"
RDEPENDS_task-poky-x11-sato = "\
    matchbox-desktop \
    matchbox-session-sato \
    matchbox-keyboard \
    matchbox-stroke \
    matchbox-config-gtk \
    matchbox-themes-gtk \
    xcursor-transparent-theme \
    sato-icon-theme \
    settings-daemon \
    gtk-sato-engine \
    $(NETWORK_MANAGER)"

