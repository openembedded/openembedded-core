DESCRIPTION = "Dropbear SSH task for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r0"

PACKAGES = "\
    packagegroup-core-ssh-dropbear \
    packagegroup-core-ssh-dropbear-dbg \
    packagegroup-core-ssh-dropbear-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_packagegroup-core-ssh-dropbear = "dropbear"
RDEPENDS_packagegroup-core-ssh-dropbear-dbg = "dropbear-dbg"
RDEPENDS_packagegroup-core-ssh-dropbear-dev = "dropbear-dev"
