#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "Debug tools tasks for OE-Core"
LICENSE = "MIT"

inherit packagegroup

PR = "r1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

MTRACE = ""
MTRACE_libc-glibc = "libc-mtrace"

RDEPENDS_${PN} = "\
    gdb \
    gdbserver \
    tcf-agent \
    openssh-sftp-server \
    rsync \
    strace \
    ${MTRACE} \
    "
