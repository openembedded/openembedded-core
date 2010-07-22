#
# Copyright (C) 2010 Intel Corporation
#

DESCRIPTION = "Create Small Image Tasks"
PR = "r0"

PACKAGES = "\
    task-poky-small-utils \
    task-poky-small-utils-dbg \
    task-poky-small-utils-dev \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-poky-small-utils = "\
    bash \
    coreutils \
    file \
    findutils \
    ncurses \
    psmisc \
    sed \
    time \
    zlib \
    udev \
    udev-extraconf \
#    gawk \
#    grep \
#    gzip \
#    makedev \
#    mktemp \
#    net-tools \
#    procps \
#    passwd \
#    tar \
#    util-linux \
    "
