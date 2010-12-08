#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "Tools tasks for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r8"

PACKAGES = "\
    task-poky-tools-debug \
    task-poky-tools-debug-dbg \
    task-poky-tools-debug-dev \
    task-poky-tools-profile \
    task-poky-tools-profile-dbg \
    task-poky-tools-profile-dev \
    task-poky-tools-testapps \
    task-poky-tools-testapps-dbg \
    task-poky-tools-testapps-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

# kexec-tools doesn't work on Mips
KEXECTOOLS ?= "kexec-tools"
KEXECTOOLS_mips ?= ""
KEXECTOOLS_mipsel ?= ""
KEXECTOOLS_powerpc ?= ""

RDEPENDS_task-poky-tools-debug = "\
    gdb \
    gdbserver \
    tcf-agent \
    rsync \
    strace"

RDEPENDS_task-poky-tools-profile = "\
    oprofile \
    oprofileui-server \
    powertop \
    latencytop \
    lttng-control \
    lttng-viewer"

RRECOMMENDS_task-poky-tools-profile = "\
    perf \
    trace-cmd \
    kernel-module-oprofile \
    sysprof \
    "

#    exmap-console
#    exmap-server

# At present we only build lttng-ust on
# qemux86/qemux86-64/qemuppc/emenlow/atom-pc since upstream liburcu
# (which is required by lttng-ust) may not build on other platforms, like
# MIPS and qemu ARMv5te that poky uses now.
RDEPENDS_task-poky-tools-profile_append_qemux86 = " valgrind lttng-ust"
RDEPENDS_task-poky-tools-profile_append_qemux86-64 = " lttng-ust"
RDEPENDS_task-poky-tools-profile_append_qemuppc = " lttng-ust"
RDEPENDS_task-poky-tools-profile_append_emenlow = " lttng-ust"
RDEPENDS_task-poky-tools-profile_append_atom-pc = " lttng-ust"

RDEPENDS_task-poky-tools-testapps = "\
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
