#
# Copyright (C) 2007 OpenedHand Ltd.
#

SUMMARY = "GNOME Mobile And Embedded SDK (host tools)"
LICENSE = "MIT"
PR = "r13"

inherit packagegroup

require packagegroup-sdk-gmae.inc

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-sdk-gmae"
RREPLACES_${PN} = "task-core-sdk-gmae"
RCONFLICTS_${PN} = "task-core-sdk-gmae"

RDEPENDS_${PN} = "\
    packagegroup-core-sdk \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
