#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r0"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    task-self-hosted \
    task-self-hosted-debug \
    task-self-hosted-sdk \
    task-self-hosted-extended \
    task-self-hosted-graphics \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-self-hosted = "\
    task-self-hosted-debug \
    task-self-hosted-sdk \
    task-self-hosted-extended \
    task-self-hosted-graphics \
    "

# eglibc-utils: for rpcgen
RDEPENDS_task-self-hosted-sdk = "\
    sed \
    mktemp \
    eglibc-utils \
    autoconf \
    automake \
    binutils-symlinks \
    binutils \
    cpp \
    cpp-symlinks \
    ccache \
    gcc \
    gcc-symlinks \
    g++ \
    g++-symlinks \
    gettext \
    make \
    intltool \
    libstdc++ \
    libstdc++-dev \
    libtool \
    perl-module-re \
    perl-module-text-wrap \
    coreutils \
    diffutils \
    pkgconfig \
    findutils \
    quilt \
    less \
    distcc \
    ldd \
    file \
    tcl \
    "

RDEPENDS_task-self-hosted-debug = " \
    gdb \
    gdbserver \
    tcf-agent \
    rsync \
    strace"


RDEPENDS_task-self-hosted-extended = "\
    binutils \
    bzip2 \
    chkconfig \
    chrpath \
    curl \
    dhcp-client \
    diffstat \
    diffutils \
    elfutils \
    expat \
    gamin \
    gdbm \
    git \
    gettext \
    gettext-runtime \
    grep \
    groff \
    gawk \
    hdparm \
    libaio \
    libxml2 \
    libusb1 \
    lrzsz \
    lsof \
    lzo \
    man \
    man-pages \
    mc \
    mdadm \
    minicom \
    mtools \
    ncurses \
    neon \
    openssl \
    opkg \
    opkg-utils \
    parted \
    patch \
    perl \
    perl-modules \
    perl-dev \
    pth \
    python \
    python-compile \
    python-compiler \
    python-core \
    python-curses \
    python-datetime \
    python-distutils \
    python-elementtree \
    python-fcntl \
    python-logging \
    python-misc \
    python-multiprocessing \
    python-netclient \
    python-netserver \
    python-pickle \
    python-re \
    python-rpm \
    python-shell \
    python-sqlite3 \
    python-subprocess \
    python-textutils \
    python-xmlrpc \
    python-email \
    python-unixadmin \
    python-compression \
    quota \
    readline \
    rpm \
    screen \
    setserial \
    strace \
    subversion \
    sysstat \
    tar \
    gzip \
    tcl \
    texi2html \
    texinfo \
    usbutils \
    unzip \
    watchdog \
    wget \
    which \
    xinetd \
    zip \
    zlib \
    cpio \
    "


RDEPENDS_task-self-hosted-graphics = "\
    python-pygtk \
    mesa-dri \
    mesa-dri-dev \
    libglu \
    libglu-dev \
    libsdl \
    libsdl-dev \
    libx11-dev \
    "
