DESCRIPTION = "OpenSSH SSH task for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r0"

PACKAGES = "\
    task-poky-ssh-openssh \
    task-poky-ssh-openssh-dbg \
    task-poky-ssh-openssh-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-ssh-openssh = "openssh"
RDEPENDS_task-poky-ssh-openssh-dbg = "openssh-dbg"
RDEPENDS_task-poky-ssh-openssh-dev = "openssh-dev"
