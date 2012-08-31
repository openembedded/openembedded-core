#
# Copyright (C) 2007 OpenedHand Ltd.
#

SUMMARY = "Clutter package groups"
LICENSE = "MIT"

PR = "r4"

inherit packagegroup

PACKAGES = "\
    ${PN}-core \
    "

SUMMARY_${PN}-core = "Clutter graphics library"
RDEPENDS_${PN}-core = "\
    clutter-1.8 \
    clutter-gst-1.8 \
    clutter-gtk-1.8 \
    "
