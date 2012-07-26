#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Small Image Tasks"
PR = "r9"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

PACKAGES = "\
    packagegroup-core-lsb \
    packagegroup-core-sys-extended \
    packagegroup-core-db \
    packagegroup-core-misc \
    packagegroup-core-perl \
    packagegroup-core-python \
    packagegroup-core-tcl \
    packagegroup-core-lsb-perl-add \
    packagegroup-core-lsb-python-add \
    packagegroup-core-lsb-graphic-add \
    packagegroup-core-lsb-runtime-add \
    packagegroup-core-lsb-command-add \
    "


RDEPENDS_packagegroup-core-lsb = "\
    packagegroup-core-sys-extended \
    packagegroup-core-db \
    packagegroup-core-misc \
    packagegroup-core-perl \
    packagegroup-core-python \
    packagegroup-core-tcl \
    packagegroup-core-lsb-perl-add \
    packagegroup-core-lsb-python-add \
    packagegroup-core-lsb-graphic-add \
    packagegroup-core-lsb-runtime-add \
    packagegroup-core-lsb-command-add \
    "

RDEPENDS_packagegroup-core-sys-extended = "\
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
    ${PTH} \
    quota \
    screen \
    setserial \
    sysstat \
    unzip \
    watchdog \
    which \
    xinetd \
    zip \
    lsbinitscripts \
    "

RDEPENDS_packagegroup-core-db = "\
    db \
    sqlite3 \
    "

RDEPENDS_packagegroup-core-perl = "\
    gdbm \
    perl \
    zlib \
    "


RDEPENDS_packagegroup-core-python = "\
    expat \
    gdbm \
    gmp \
    ncurses \
    openssl \
    python \
    readline \
    zip \
    "

RDEPENDS_packagegroup-core-tcl = "\
    tcl \
    "

RDEPENDS_packagegroup-core-misc = "\
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

RDEPENDS_packagegroup-core-lsb-command-add = "\
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

RDEPENDS_packagegroup-core-lsb-perl-add = "\
    perl-modules \
    perl-misc \
    perl-pod \
"

RDEPENDS_packagegroup-core-lsb-python-add = "\
    python-modules \
    python-misc \
"

RDEPENDS_packagegroup-core-lsb-graphic-add = "\
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
    gdk-pixbuf-xlib \
    liberation-fonts \
    gtk+ \
    atk \
    libasound \
    ${@base_contains("DISTRO_FEATURES", "opengl", "libqtopengl4", "", d)} \
"

RDEPENDS_packagegroup-core-lsb-runtime-add = "\
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
    eglibc-utils \
"

PTH = "pth"
PTH_libc-uclibc = ""

