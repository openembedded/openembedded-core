#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r2"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    task-self-hosted \
    task-self-hosted-debug \
    task-self-hosted-sdk \
    task-self-hosted-extended \
    task-self-hosted-graphics \
    task-self-hosted-host-tools \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-self-hosted = "\
    task-self-hosted-debug \
    task-self-hosted-sdk \
    task-self-hosted-extended \
    task-self-hosted-graphics \
    task-self-hosted-host-tools \
    "

RDEPENDS_task-self-hosted-host-tools = "\
    dhcp-client \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-fsck \
    e2fsprogs-mke2fs \
    e2fsprogs-tune2fs \
    genext2fs \
    hdparm \
    iptables \
    kernel-module-tun \
    kernel-module-iptable-raw \
    kernel-module-iptable-nat \
    kernel-module-iptable-mangle \
    kernel-module-iptable-filter \
    mc \
    parted \
    screen \
    "

# eglibc-utils: for rpcgen
RDEPENDS_task-self-hosted-sdk = "\
    autoconf \
    automake \
    binutils-symlinks \
    binutils \
    cpp \
    cpp-symlinks \
    ccache \
    coreutils \
    diffutils \
    distcc \
    eglibc-utils \
    file \
    findutils \
    gcc \
    gcc-symlinks \
    g++ \
    g++-symlinks \
    gettext \
    intltool \
    ldd \
    less \
    libstdc++ \
    libstdc++-dev \
    libtool \
    make \
    mktemp \
    perl-module-re \
    perl-module-text-wrap \
    pkgconfig \
    quilt \
    sed \
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
    cpio \
    curl \
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
    libaio \
    libxml2 \
    libusb1 \
    lrzsz \
    lsof \
    lzo \
    man \
    man-pages \
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
    perl-dev \
    perl-modules \
    perl-pod \
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
    setserial \
    subversion \
    sudo \
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
    "


RDEPENDS_task-self-hosted-graphics = "\
    python-pygtk \
    libgl \
    libgl-dev \
    libglu \
    libglu-dev \
    libsdl \
    libsdl-dev \
    libx11-dev \
    "
