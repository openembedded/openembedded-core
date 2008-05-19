#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "NFS tasks for Poky"
PR = "r0"

PACKAGES = "\
    task-poky-nfs-server \
    task-poky-nfs-server-dbg \
    task-poky-nfs-server-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-nfs-server = "\
    nfs-utils"

# rpcinfo can be useful
RRECOMMENDS_task-poky-nfs-server = "\
    glibc-utils"
