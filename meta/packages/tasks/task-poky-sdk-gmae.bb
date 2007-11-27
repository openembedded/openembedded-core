#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTON = "Gnome Mobile And Embedded Software Development Kit for OpenedHand Poky"
DEPENDS = "task-poky-sdk"

ALLOW_EMPTY = "1"

PACKAGES = "\
    task-poky-sdk-gmae \
    task-poky-sdk-gmae-dbg"

RDEPENDS_task-poky-sdk-gmae = "\
    task-poky-sdk \
    gtk+-dev \
    eds-dbus-dev \
    gstreamer-dev \
    bluez-libs-dev \
    gnome-vfs-dev \
    gconf-dbus-dev \
    avahi-dev \
    libtelepathy-dev \
    "
