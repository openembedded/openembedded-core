#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Small Image Tasks"
PR = "r0"

PACKAGES = "\
    task-poky-small \
    task-poky-basic-libs \
    task-poky-basic-libs-dbg \
    task-poky-basic-libs-dev \
    task-poky-basic-utils \
    task-poky-basic-utils-dbg \
    task-poky-basic-utils-dev \
    task-poky-minimal-extras \
    task-poky-minimal-extras-dbg \
    task-poky-minimal-extras-dev \
    task-poky-pkg-managment-opkg \
    task-poky-pkg-managment-opkg-dbg \
    task-poky-pkg-managment-opkg-dev \
    task-poky-network-services \
    task-poky-network-services-dbg \
    task-poky-network-services-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-minimal-extras = "\
     makedevs \
     ncurses \
     zlib \
     udev \
     udev-extraconf \
     expat \
     "

RDEPENDS_task-poky-pkg-managment-opkg = "\
     curl \
     gnutls \
     gpgme \
     libgcrypt \
     libgpg-error \
     lzo \
     opkg-config-base \
     opkg-collateral \
     opkg \
     pth \
     "

RDEPENDS_task-poky-network-services = "\
#    iproute \
#    iputils \
#    iptables \
    nfs-utils \
    "

RDEPENDS_task-poky-shells = "\
    bash \
    mktemp \
    

RDEPENDS_task-poky-misc = "\
    strace \
    lsof \
    elfutils \
    usbutils \
    zlib \
    libusb \
    libstdcxx \
    bluez4 \
    "

RDEPENDS_task-poky-basic-libs = "\
#    libssh2 \
    dbus-glib \
#    nfs-utils-lib \
    "

RDEPENDS_task-poky-basic-utils = "\
#    openssh \
    openssl \
    pam \
    sudo \
    beecrypt \
    curl \
    elfutils \
    neon \
#    mktemp \
    rpm \
    bzip2 \
#    crontabs \
    dbus \
    python-dbus \
    e2fsprogs \
    gdbm \
    hal \
    less \
    popt \
    portmap \
    readline \
#    cron \
#    yaffs2 \
    "

RDEPENDS_task-poky-extended-libs = "\
#    cracklib \
    glib-2.0 \
    libcap \
    libevent \
    "

RDEPENDS_task-poky-extended-utils = "\
#    acl \
    at \
    attr \
#    bc \
    binutils \
#    cpio \
#    cracklib \
#    logrotate \
#    pax \
#    shadow \
    sqlite3 \
#    syslog \
#    ed \
#    lsb \
#    mailx \
#    man \
#    sendmail \
    tar \
    tcl \
    "   

RDEPENDS_task-poky-python = "\
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
    zlib \
    "
