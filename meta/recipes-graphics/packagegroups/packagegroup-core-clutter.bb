#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
LICENSE = "MIT"

PR = "r4"

inherit packagegroup

PACKAGES = "\
    ${PN}-core \
    "

RDEPENDS_${PN}-core = "\
    clutter-1.8 \
    clutter-gst-1.8 \
    clutter-gtk-1.8 \
    "
