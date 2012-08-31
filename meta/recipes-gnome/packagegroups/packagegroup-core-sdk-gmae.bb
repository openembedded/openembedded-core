#
# Copyright (C) 2007 OpenedHand Ltd.
#

SUMMARY = "GNOME Mobile And Embedded SDK (host tools)"
LICENSE = "MIT"
PR = "r12"

inherit packagegroup

require packagegroup-sdk-gmae.inc

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} = "\
    packagegroup-core-sdk \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
