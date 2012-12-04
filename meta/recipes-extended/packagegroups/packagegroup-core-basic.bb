#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Basic Image Tasks"
PR = "r6"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-core-basic \
    packagegroup-core-basic-libs \
    packagegroup-core-basic-utils \
    packagegroup-core-basic-extended \
    packagegroup-core-dev-utils \
    packagegroup-core-multiuser \
    packagegroup-core-initscripts \
    packagegroup-core-sys-services \
    "

python __anonymous () {
    # For backwards compatibility after rename
    packages = d.getVar("PACKAGES", True).split()
    for pkg in packages:
        d.appendVar("RPROVIDES_%s" % pkg, pkg.replace("packagegroup-core", "task-core"))
        d.appendVar("RREPLACES_%s" % pkg, pkg.replace("packagegroup-core", "task-core"))
        d.appendVar("RCONFLICTS_%s" % pkg, pkg.replace("packagegroup-core", "task-core"))
}


RDEPENDS_packagegroup-core-basic = "\
    packagegroup-core-basic-libs \
    packagegroup-core-basic-utils \
    packagegroup-core-basic-extended \
    packagegroup-core-dev-utils \
    packagegroup-core-multiuser \
    packagegroup-core-initscripts \
    packagegroup-core-sys-services \
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

VIRTUAL-RUNTIME_initscripts ?= "initscripts"
VIRTUAL-RUNTIME_init_manager ?= "sysvinit"
RDEPENDS_packagegroup-core-initscripts = "\
    ${VIRTUAL-RUNTIME_initscripts} \
    ${VIRTUAL-RUNTIME_init_manager} \
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

