#
# Copyright (C) 2007 OpenedHand Ltd.
#

SUMMARY = "GNOME Mobile And Embedded SDK (target tools)"
LICENSE = "MIT"
PR = "r15"

inherit packagegroup

require packagegroup-sdk-gmae.inc

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-standalone-gmae-sdk-target"

RDEPENDS_${PN} = "\
    packagegroup-core-standalone-sdk-target \
    libglade-dev \
    ${SDK-GMAE} \
    ${SDK-EXTRAS}"
