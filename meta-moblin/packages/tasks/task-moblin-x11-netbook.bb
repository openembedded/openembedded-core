#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Netbook GUI Tasks for Moblin"
PR = "r9"

PACKAGES = "\
    task-moblin-x11-netbook \
    task-moblin-x11-netbook-dbg \
    task-moblin-x11-netbook-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

EXTRA_MOBLIN_PACKAGES ?= ""
RDEPENDS_task-moblin-x11-netbook = "\
    bickley \
    mojito \
    mutter \
    matchbox-config-gtk \
    xcursor-transparent-theme \
    settings-daemon \
    librest \
    nbtk \
    mutter-moblin \
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
