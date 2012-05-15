#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "Debug tools tasks for OE-Core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGES = "\
    ${PN} \
    ${PN}-dbg \
    ${PN}-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

MTRACE = ""
MTRACE_libc-glibc = "libc-mtrace"

RDEPENDS_${PN} = "\
    gdb \
    gdbserver \
    tcf-agent \
    rsync \
    strace \
    ${MTRACE} \
    "
