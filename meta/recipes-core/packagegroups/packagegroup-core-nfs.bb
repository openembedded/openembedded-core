#
# Copyright (C) 2008 OpenedHand Ltd.
#

SUMMARY = "NFS package groups"
LICENSE = "MIT"
PR = "r2"

inherit packagegroup

PACKAGES = "${PN}-server"

# For backwards compatibility after rename
RPROVIDES_${PN}-server = "task-core-nfs-server"
RREPLACES_${PN}-server = "task-core-nfs-server"
RCONFLICTS_${PN}-server = "task-core-nfs-server"

SUMMARY_${PN}-server = "NFS server"
RDEPENDS_${PN}-server = "\
    nfs-utils \
    nfs-utils-client \
    "
