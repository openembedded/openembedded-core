#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r10"
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
    connman \
    connman-plugin-ethernet \
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
    lsb \
    mc \
    parted \
    pseudo \
    screen \
    vte \
    "

# eglibc-utils: for rpcgen
RDEPENDS_task-self-hosted-sdk = "\
    autoconf \
    automake \
    binutils \
    binutils-symlinks \
    ccache \
    coreutils \
    cpp \
    cpp-symlinks \
    distcc \
    eglibc-utils \
    eglibc-gconv-ibm850 \
    file \
    findutils \
    g++ \
    g++-symlinks \
    gcc \
    gcc-symlinks \
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
    "

RDEPENDS_task-self-hosted-debug = " \
    gdb \
    gdbserver \
    rsync \
    strace \
    tcf-agent"


RDEPENDS_task-self-hosted-extended = "\
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
    gawk \
    gdbm \
    gettext \
    gettext-runtime \
    git \
    grep \
    groff \
    gzip \
    hicolor-icon-theme \
    libaio \
    libusb1 \
    libxml2 \
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
    nfs-utils \
    nfs-utils-client \
    openssl \
    opkg \
    opkg-utils \
    patch \
    perl \
    perl-dev \
    perl-modules \
    perl-pod \
    pth \
    python \
    python-compile \
    python-compiler \
    python-compression \
    python-core \
    python-curses \
    python-datetime \
    python-distutils \
    python-elementtree \
    python-email \
    python-fcntl \
    python-logging \
    python-misc \
    python-multiprocessing \
    python-netclient \
    python-netserver \
    python-pickle \
    python-pkgutil \
    python-re \
    python-rpm \
    python-shell \
    python-sqlite3 \
    python-subprocess \
    python-textutils \
    python-unixadmin \
    python-xmlrpc \
    quota \
    readline \
    rpm \
    setserial \
    socat \
    subversion \
    sudo \
    sysstat \
    tar \
    tcl \
    texi2html \
    texinfo \
    unzip \
    usbutils \
    watchdog \
    wget \
    which \
    xinetd \
    zip \
    zlib \
    "


RDEPENDS_task-self-hosted-graphics = "\
    builder \
    libgl \
    libgl-dev \
    libglu \
    libglu-dev \
    libsdl \
    libsdl-dev \
    libx11-dev \
    python-pygtk \
    gtk-theme-clearlooks \
    "
