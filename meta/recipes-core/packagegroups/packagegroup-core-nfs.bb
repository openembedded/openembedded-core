#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "NFS tasks for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r1"

PACKAGES = "\
    packagegroup-core-nfs-server \
    packagegroup-core-nfs-server-dbg \
    packagegroup-core-nfs-server-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_packagegroup-core-nfs-server = "\
    nfs-utils \
    nfs-utils-client \
    "
