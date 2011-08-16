#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "Tools tasks for OE-Core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r11"

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

# systemtap needs elfutils which is not fully buildable on uclibc
# hence we exclude it from uclibc based builds
SYSTEMTAP = "systemtap"
SYSTEMTAP_libc-uclibc = ""

# lttng-ust uses sched_getcpu() which is not there on uclibc
# for some of the architectures it can be patched to call the
# syscall directly but for x86_64 __NR_getcpu is a vsyscall
# which means we can not use syscall() to call it. So we ignore
# it for x86_64/uclibc

LTTNGUST = "lttng-ust"
LTTNGUST_libc-uclibc = ""

#    exmap-console
#    exmap-server

# At present we only build lttng-ust on
# qemux86/qemux86-64/qemuppc/qemuarm/emenlow/atom-pc since upstream liburcu
# (which is required by lttng-ust) may not build on other platforms, like
# MIPS.
RDEPENDS_task-core-tools-profile_append_qemux86 = " valgrind lttng-ust ${SYSTEMTAP}"
RDEPENDS_task-core-tools-profile_append_qemux86-64 = " ${LTTNGUST} ${SYSTEMTAP}"
RDEPENDS_task-core-tools-profile_append_qemuppc = " ${LTTNGUST} ${SYSTEMTAP}"
RDEPENDS_task-core-tools-profile_append_qemuarm = " ${LTTNGUST}"

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
    mesa-demos \
    x11perf \
    xrestop \
    xwininfo \
    xprop \
    xvideo-tests \
    clutter-box2d-1.6 \
    "
