#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Small Image Tasks"
PR = "r2"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    task-poky-lsb \
    task-poky-sys-extended \
    task-poky-sys-extended-dbg \
    task-poky-sys-extended-dev \
    task-poky-db \
    task-poky-db-dbg \
    task-poky-db-dev \
    task-poky-misc \
    task-poky-misc-dbg \
    task-poky-misc-dev \
    task-poky-perl \
    task-poky-perl-dbg \
    task-poky-perl-dev \
    task-poky-python \
    task-poky-python-dbg \
    task-poky-python-dev \
    task-poky-tcl \
    task-poky-tcl-dbg \
    task-poky-tcl-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-lsb = "\
    task-poky-sys-extended \
    task-poky-db \
    task-poky-misc \
    task-poky-perl \
    task-poky-python \
    task-poky-tcl \
    "

#
# GPLv2 Should List
RDEPENDS_task-poky-sys-extended = "\
    curl \
    dhcp-client \
    diffutils \
    gamin \
    hdparm \
    libaio \
    libxml2 \
    lrzsz \
    lzo \
    mc \
    mdadm \
    minicom \
    neon \
    parted \
    pth \
    quota \
    screen \
    setserial \
    sysstat \
    unzip \
    watchdog \
    which \
    xinetd \
    zip \
    "

RDEPENDS_task-poky-db = "\
    db \
    sqlite3 \
    "

RDEPENDS_task-poky-perl = "\
    gdbm \
    perl \
    zlib \
    "


RDEPENDS_task-poky-python = "\
    expat \
    gdbm \
    gmp \
    ncurses \
    openssl \
    python \
    readline \
    zip \
    "

RDEPENDS_task-poky-tcl = "\
    tcl \
    "

RDEPENDS_task-poky-misc = "\
    elfutils \
    gettext \
    groff \
    lsb \
    lsof \
    man \
    man-pages \
    ncurses \
    strace \
    libusb1 \
    usbutils \
    zlib \
    "
