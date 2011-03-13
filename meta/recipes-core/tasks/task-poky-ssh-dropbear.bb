DESCRIPTION = "Dropbear SSH task for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r0"

PACKAGES = "\
    task-poky-ssh-dropbear \
    task-poky-ssh-dropbear-dbg \
    task-poky-ssh-dropbear-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-ssh-dropbear = "dropbear"
RDEPENDS_task-poky-ssh-dropbear-dbg = "dropbear-dbg"
RDEPENDS_task-poky-ssh-dropbear-dev = "dropbear-dev"
