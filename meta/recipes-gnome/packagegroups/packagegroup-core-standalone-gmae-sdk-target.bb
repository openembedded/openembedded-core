#
# Copyright (C) 2007 OpenedHand Ltd.
#

SUMMARY = "GNOME Mobile And Embedded SDK (target tools)"
LICENSE = "MIT"
PR = "r14"

inherit packagegroup

require packagegroup-sdk-gmae.inc

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} = "\
    packagegroup-core-standalone-sdk-target \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
