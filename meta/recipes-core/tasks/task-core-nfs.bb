#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "NFS tasks for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r1"

PACKAGES = "\
    task-core-nfs-server \
    task-core-nfs-server-dbg \
    task-core-nfs-server-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-core-nfs-server = "\
    nfs-utils \
    nfs-utils-client \
    "

# rpcinfo can be useful but only with glibc images
GLIBC_DEPENDENCIES = "glibc-utils"

RRECOMMENDS_task-core-nfs-server_append_libc-glibc = " ${GLIBC_DEPENDENCIES}"

