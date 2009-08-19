#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Netbook GUI Tasks for Moblin"
PR = "r17"

PACKAGES = "\
    task-moblin-x11-netbook \
    task-moblin-x11-netbook-dbg \
    task-moblin-x11-netbook-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

#    matchbox-config-gtk \
#    xcursor-transparent-theme \

EXTRA_MOBLIN_PACKAGES ?= ""
RDEPENDS_task-moblin-x11-netbook = "\
    bickley \
    mojito \
    mutter \
    droid-fonts \
    gnome-settings-daemon \
    librest \
    nbtk \
    nautilus \
    mutter-moblin \
    moblin-panel-applications \
    moblin-panel-media \
    moblin-panel-myzone \
    moblin-panel-pasteboard \
    moblin-panel-people \
    moblin-panel-status \
    moblin-gtk-engine \
    moblin-menus \
    moblin-user-skel \
    bisho \
    moblin-web-browser \
    dalston \
    hornsey \
    anerley \
    bognor-regis \
    moblin-icon-theme \
    moblin-cursor-theme \
    moblin-sound-theme \
    matchbox-session-netbook \
    carrick \
    ${EXTRA_MOBLIN_PACKAGES}"
