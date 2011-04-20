#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "NFS tasks for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r0"

PACKAGES = "\
    task-poky-nfs-server \
    task-poky-nfs-server-dbg \
    task-poky-nfs-server-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-nfs-server = "\
    nfs-utils"

# rpcinfo can be useful but only with glibc images
GLIBC_DEPENDENCIES = "glibc-utils"

RRECOMMENDS_task-poky-nfs-server_append_linux = "${GLIBC_DEPENDENCIES}"
RRECOMMENDS_task-poky-nfs-server_append_linux-gnueabi = "${GLIBC_DEPENDENCIES}"
