#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Clutter Tasks for OpenedHand Poky"
LICENSE = "MIT"

PR = "r4"

inherit packagegroup

PACKAGES = "\
    packagegroup-core-clutter-core \
    packagegroup-core-clutter-apps \
    packagegroup-core-clutter-tests \
    "

RDEPENDS_packagegroup-core-clutter-core = "\
    clutter-1.8 \
    clutter-gst-1.8 \
    clutter-gtk-1.8 \
    "

#RDEPENDS_packagegroup-core-clutter-tests = "\
#    clutter-gst-1.8-examples \
#    clutter-gtk-1.8-examples"

#RDEPENDS_packagegroup-core-clutter-apps = "\
#    clutter-box2d "
