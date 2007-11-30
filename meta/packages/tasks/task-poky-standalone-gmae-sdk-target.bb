#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTON = "Gnome Mobile And Embedded Software Development Kit for OpenedHand Poky"
DEPENDS = "task-poky-standalone-sdk-target"
PR = "r1"

ALLOW_EMPTY = "1"

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    task-poky-standalone-sdk-target \
    gtk+-dev \
    eds-dbus-dev \
    gstreamer-dev \
    bluez-libs-dev \
    gnome-vfs-dev \
    gconf-dbus-dev \
    avahi-dev \
    libtelepathy-dev \
    "
