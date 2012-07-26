#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r4"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

PACKAGES = "\
    packagegroup-core-basic \
    packagegroup-core-base-utils \
    packagegroup-core-basic-libs \
    packagegroup-core-basic-utils \
    packagegroup-core-basic-extended \
    packagegroup-core-dev-utils \
    packagegroup-core-multiuser \
    packagegroup-core-initscripts \
    packagegroup-core-rpm \
    packagegroup-core-sys-services \
    "


RDEPENDS_packagegroup-core-basic = "\
    packagegroup-core-base-utils \
    packagegroup-core-basic-libs \
    packagegroup-core-basic-utils \
    packagegroup-core-basic-extended \
    packagegroup-core-dev-utils \
    packagegroup-core-multiuser \
    packagegroup-core-initscripts \
    packagegroup-core-rpm \
    packagegroup-core-sys-services \
    "

RDEPENDS_packagegroup-core-base-utils = "\
    busybox \
    "

RDEPENDS_packagegroup-core-basic-libs = "\
    glib-2.0 \
    "

RDEPENDS_packagegroup-core-basic-utils = "\
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

RDEPENDS_packagegroup-core-basic-extended = "\
    iproute2 \
    iputils \
    iptables \
    module-init-tools \
    openssl \
    "

RDEPENDS_packagegroup-core-dev-utils = "\
    byacc \
    diffutils \
    m4 \
    make \
    patch \
    "

RDEPENDS_packagegroup-core-initscripts = "\
    initscripts \
    sysvinit \
    ethtool \
    mingetty \
    sysklogd \
    "

RDEPENDS_packagegroup-core-multiuser = "\
    cracklib \
    gzip \
    libuser \
    libpam \
    shadow \
    sudo \
    "

RDEPENDS_packagegroup-core-rpm = "\
    beecrypt \
    rpm \
    zypper \
    "

RDEPENDS_packagegroup-core-sys-services = "\
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
    rpcbind \
    sysfsutils \
    tcp-wrappers \
    tzdata \
    "

