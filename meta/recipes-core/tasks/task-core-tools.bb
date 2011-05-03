#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "Tools tasks for OE-Core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r10"

PACKAGES = "\
    task-core-tools-debug \
    task-core-tools-debug-dbg \
    task-core-tools-debug-dev \
    task-core-tools-profile \
    task-core-tools-profile-dbg \
    task-core-tools-profile-dev \
    task-core-tools-testapps \
    task-core-tools-testapps-dbg \
    task-core-tools-testapps-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

# kexec-tools doesn't work on Mips
KEXECTOOLS ?= "kexec-tools"
KEXECTOOLS_mips ?= ""
KEXECTOOLS_mipsel ?= ""
KEXECTOOLS_powerpc ?= ""

RDEPENDS_task-core-tools-debug = "\
    gdb \
    gdbserver \
    tcf-agent \
    rsync \
    strace"

RDEPENDS_task-core-tools-profile = "\
    oprofile \
    oprofileui-server \
    powertop \
    latencytop \
    lttng-control \
    lttng-viewer"

RRECOMMENDS_task-core-tools-profile = "\
    perf \
    trace-cmd \
    kernel-module-oprofile \
    blktrace \
    sysprof \
    "

#    exmap-console
#    exmap-server

# At present we only build lttng-ust on
# qemux86/qemux86-64/qemuppc/qemuarm/emenlow/atom-pc since upstream liburcu
# (which is required by lttng-ust) may not build on other platforms, like
# MIPS.
RDEPENDS_task-core-tools-profile_append_qemux86 = " valgrind lttng-ust systemtap"
RDEPENDS_task-core-tools-profile_append_qemux86-64 = " lttng-ust systemtap"
RDEPENDS_task-core-tools-profile_append_qemuppc = " lttng-ust systemtap"
RDEPENDS_task-core-tools-profile_append_qemuarm = " lttng-ust"

RDEPENDS_task-core-tools-testapps = "\
    blktool \
    fstests \
    tslib-calibrate \
    tslib-tests \
    lrzsz \
    ${KEXECTOOLS} \
    alsa-utils-amixer \
    alsa-utils-aplay \
    owl-video \
    gst-meta-video \
    gst-meta-audio \
    x11perf \
    xrestop \
    xwininfo \
    xprop \
    xvideo-tests"
