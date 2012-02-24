#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Small Image Tasks"
PR = "r7"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    task-core-lsb \
    task-core-sys-extended \
    task-core-sys-extended-dbg \
    task-core-sys-extended-dev \
    task-core-db \
    task-core-db-dbg \
    task-core-db-dev \
    task-core-misc \
    task-core-misc-dbg \
    task-core-misc-dev \
    task-core-perl \
    task-core-perl-dbg \
    task-core-perl-dev \
    task-core-python \
    task-core-python-dbg \
    task-core-python-dev \
    task-core-tcl \
    task-core-tcl-dbg \
    task-core-tcl-dev \
    task-core-lsb-perl-add \
    task-core-lsb-python-add \
    task-core-lsb-graphic-add \
    task-core-lsb-runtime-add \
    task-core-lsb-command-add \
    "


ALLOW_EMPTY = "1"

RDEPENDS_task-core-lsb = "\
    task-core-sys-extended \
    task-core-db \
    task-core-misc \
    task-core-perl \
    task-core-python \
    task-core-tcl \
    task-core-lsb-perl-add \
    task-core-lsb-python-add \
    task-core-lsb-graphic-add \
    task-core-lsb-runtime-add \
    task-core-lsb-command-add \
    "

RDEPENDS_task-core-sys-extended = "\
    binutils \
    binutils-symlinks \
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

RDEPENDS_task-core-db = "\
    db \
    sqlite3 \
    "

RDEPENDS_task-core-perl = "\
    gdbm \
    perl \
    zlib \
    "


RDEPENDS_task-core-python = "\
    expat \
    gdbm \
    gmp \
    ncurses \
    openssl \
    python \
    readline \
    zip \
    "

RDEPENDS_task-core-tcl = "\
    tcl \
    "

RDEPENDS_task-core-misc = "\
    elfutils \
    gettext \
    gettext-runtime \
    groff \
    lsb \
    lsbsetup \
    lsbtest \
    lsof \
    man \
    man-pages \
    ncurses \
    strace \
    libusb1 \
    usbutils \
    zlib \
    "

RDEPENDS_task-core-lsb-command-add = "\
    localedef \
    fontconfig-utils \
    mailx \
    msmtp \
    chkconfig \
    xdg-utils \
    foomatic-filters \
    cups \
    ghostscript \
"

RDEPENDS_task-core-lsb-perl-add = "\
    perl-modules \
    perl-misc \
    perl-pod \
"

RDEPENDS_task-core-lsb-python-add = "\
    python-modules \
    python-misc \
"

RDEPENDS_task-core-lsb-graphic-add = "\
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
    libx11-locale \
    xorg-minimal-fonts \
    gdk-pixbuf-loader-ico \
    gdk-pixbuf-loader-bmp \
    gdk-pixbuf-loader-ani \
    liberation-fonts \
    gtk+ \
    atk \
    libasound \
    ${@base_contains("DISTRO_FEATURES", "opengl", "libqtopengl4", "", d)} \
"

#    mesa-dri 

RDEPENDS_task-core-lsb-runtime-add = "\
    ldd \
    pam-plugin-wheel \
    e2fsprogs-mke2fs \
    mkfontdir \
    liburi-perl \
    libxml-parser-perl \
    libxml-perl \
    libxml-sax-perl \
    eglibc-localedatas \
    eglibc-gconvs \
    eglibc-charmaps \
    eglibc-binaries \
    eglibc-localedata-posix \
    eglibc-extra-nss \
    eglibc-pcprofile \
    eglibc-pic \
    eglibc-utils \
"
