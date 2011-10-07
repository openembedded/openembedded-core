#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r4"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    task-core-basic \
    task-core-base-utils \
    task-core-base-utils-dbg \
    task-core-base-utils-dev \
    task-core-basic-libs \
    task-core-basic-libs-dbg \
    task-core-basic-libs-dev \
    task-core-basic-utils \
    task-core-basic-utils-dbg \
    task-core-basic-utils-dev \
    task-core-basic-extended \
    task-core-basic-extended-dbg \
    task-core-basic-extended-dev \
    task-core-dev-utils \
    task-core-dev-utils-dbg \
    task-core-dev-utils-dev \
    task-core-multiuser \
    task-core-multiuser-dbg \
    task-core-multiuser-dev \
    task-core-initscripts \
    task-core-initscripts-dbg \
    task-core-initscripts-dev \
    task-core-rpm \
    task-core-rpm-dbg \
    task-core-rpm-dev \
    task-core-sys-services \
    task-core-sys-services-dbg \
    task-core-sys-services-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-core-basic = "\
    task-core-base-utils \
    task-core-basic-libs \
    task-core-basic-utils \
    task-core-basic-extended \
    task-core-dev-utils \
    task-core-multiuser \
    task-core-initscripts \
    task-core-rpm \
    task-core-sys-services \
    "

RDEPENDS_task-core-base-utils = "\
    busybox \
    "

RDEPENDS_task-core-basic-libs = "\
    glib-2.0 \
    "

RDEPENDS_task-core-basic-utils = "\
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

RDEPENDS_task-core-basic-extended = "\
    iproute2 \
    iputils \
    iptables \
    module-init-tools \
    openssl \
    "

RDEPENDS_task-core-dev-utils = "\
    byacc \
    diffutils \
    m4 \
    make \
    patch \
    "

RDEPENDS_task-core-initscripts = "\
    initscripts \
    sysvinit \
    ethtool \
    mingetty \
    sysklogd \
    "

RDEPENDS_task-core-multiuser = "\
    cracklib \
    gzip \
    libuser \
    libpam \
    shadow \
    sudo \
    "

RDEPENDS_task-core-rpm = "\
    beecrypt \
    rpm \
    zypper \
    "

RDEPENDS_task-core-sys-services = "\
    at \
    bzip2 \
    cronie \
    dbus \
    dbus-glib \
    python-dbus \
    elfutils \
    gzip \
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

