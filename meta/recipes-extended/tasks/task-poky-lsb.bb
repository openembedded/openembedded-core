#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Small Image Tasks"
PR = "r3"
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
    task-poky-lsb-perl-add \
    task-poky-lsb-python-add \
    task-poky-lsb-graphic-add \
    task-poky-lsb-runtime-add \
    task-poky-lsb-command-add \
    "


ALLOW_EMPTY = "1"

RDEPENDS_task-poky-lsb = "\
    task-poky-sys-extended \
    task-poky-db \
    task-poky-misc \
    task-poky-perl \
    task-poky-python \
    task-poky-tcl \
    task-poky-lsb-perl-add \
    task-poky-lsb-python-add \
    task-poky-lsb-graphic-add \
    task-poky-lsb-runtime-add \
    task-poky-lsb-command-add \
    "

#
# GPLv2 Should List
RDEPENDS_task-poky-sys-extended = "\
    chkconfig \
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

RDEPENDS_task-poky-lsb-command-add = "\
    localedef \
    fontconfig-utils \
    mailx \
    msmtp \
"
#    cups

RDEPENDS_task-poky-lsb-perl-add = "\
    perl-modules \
    perl-dev \
    perl-misc \
    perl-pod \
"

RDEPENDS_task-poky-lsb-python-add = "\
    python-modules \
"

RDEPENDS_task-poky-lsb-graphic-add = "\
    libqtcore4 \
    libqtgui4 \
    libqtsql4 \
    libqtsvg4 \
    libqtxml4 \
    libqtnetwork4 \
    libxt \
    libxxf86vm \
    libdrm \
    libglu \
    libxi \
    libxtst \
    qt4-plugin-sqldriver-sqlite \
"

RDEPENDS_task-poky-lsb-graphic-add_qemux86 = "\
    libqtopengl4 \
"
RDEPENDS_task-poky-lsb-graphic-add_atom-pc = "\
    libqtopengl4 \
"

#    mesa-dri 

RDEPENDS_task-poky-lsb-runtime-add = "\
    ldd \
    pam-plugin-wheel \
    e2fsprogs-mke2fs \
    eglibc-localedata-posix \
    mkfontdir \
    liburi-perl \
    libxml-parser-perl \
    libxml-perl \
"
