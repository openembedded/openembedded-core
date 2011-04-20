#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    task-poky-basic \
    task-poky-base-utils \
    task-poky-base-utils-dbg \
    task-poky-base-utils-dev \
    task-poky-basic-libs \
    task-poky-basic-libs-dbg \
    task-poky-basic-libs-dev \
    task-poky-basic-utils \
    task-poky-basic-utils-dbg \
    task-poky-basic-utils-dev \
    task-poky-basic-extended \
    task-poky-basic-extended-dbg \
    task-poky-basic-extended-dev \
    task-poky-dev-utils \
    task-poky-dev-utils-dbg \
    task-poky-dev-utils-dev \
    task-poky-multiuser \
    task-poky-multiuser-dbg \
    task-poky-multiuser-dev \
    task-poky-initscripts \
    task-poky-initscripts-dbg \
    task-poky-initscripts-dev \
    task-poky-rpm \
    task-poky-rpm-dbg \
    task-poky-rpm-dev \
    task-poky-sys-services \
    task-poky-sys-services-dbg \
    task-poky-sys-services-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-basic = "\
    task-poky-base-utils \
    task-poky-basic-libs \
    task-poky-basic-utils \
    task-poky-basic-extended \
    task-poky-dev-utils \
    task-poky-multiuser \
    task-poky-initscripts \
    task-poky-rpm \
    task-poky-sys-services \
    "

RDEPENDS_task-poky-base-utils = "\
    busybox \
    "

RDEPENDS_task-poky-basic-libs = "\
    glib-2.0 \
    "

RDEPENDS_task-poky-basic-utils = "\
    bash \
    acl \
    attr \
    bc \
    coreutils \
    cpio \
    e2fsprogs \
    ed \
    file \
    findutils \
    gawk \
    gmp \
    grep \
    makedevs \
    mc \
    mktemp \
    ncurses \
    net-tools \
    pax \
    popt \
    procps \
    psmisc \
    sed \
    tar \
    time \
    udev \
    util-linux \
    zlib \
    "

RDEPENDS_task-poky-basic-extended = "\
    iproute2 \
    iputils \
    iptables \
    module-init-tools \
    openssl \
    "

RDEPENDS_task-poky-dev-utils = "\
    byacc \
    diffutils \
    m4 \
    make \
    patch \
    "

RDEPENDS_task-poky-initscripts = "\
    initscripts \
    sysvinit \
    ethtool \
    mingetty \
    sysklogd \
    "

RDEPENDS_task-poky-multiuser = "\
    cracklib \
    gzip \
    libuser \
    libpam \
    shadow \
    sudo \
    "

RDEPENDS_task-poky-rpm = "\
    beecrypt \
    rpm \
    zypper \
    "

RDEPENDS_task-poky-sys-services = "\
    at \
    bzip2 \
    cronie \
    dbus \
    dbus-glib \
    python-dbus \
    elfutils \
    gzip \
    hal \
    less \
    libcap \
    libevent \
    lighttpd \
    logrotate \
    nfs-utils \
    pciutils \
    libpcre \
    portmap \
    rpcbind \
    sysfsutils \
    tcp-wrappers \
    tzdata \
    "

