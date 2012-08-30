#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "NFS tasks for Poky"
LICENSE = "MIT"
PR = "r1"

inherit packagegroup

PACKAGES = "${PN}-server"

RDEPENDS_${PN}-server = "\
    nfs-utils \
    nfs-utils-client \
    "
