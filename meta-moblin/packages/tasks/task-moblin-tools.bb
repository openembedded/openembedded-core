#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Debuggin and profiling tools tasks for Moblin"
PR = "r4"

PACKAGES = "\
    task-moblin-tools-debug \
    task-moblin-tools-debug-dbg \
    task-moblin-tools-debug-dev \
    task-moblin-tools-profile \
    task-moblin-tools-profile-dbg \
    task-moblin-tools-profile-dev \
    task-moblin-tools-testapps \
    task-moblin-tools-testapps-dbg \
    task-moblin-tools-testapps-dev \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY = "1"

RDEPENDS_task-moblin-tools-debug = "\
    gdb \
    gdbserver \
    strace"

RDEPENDS_task-moblin-tools-profile = "\
#    exmap-console \
#    exmap-server \
    oprofile \
    oprofileui-server \
    powertop \
    lttng-control \
    lttng-viewer \
    "

RDEPENDS_task-moblin-tools-profile_qemux86 += "valgrind"

RRECOMMENDS_task-moblin-tools-profile = "\
    kernel-module-oprofile"

RDEPENDS_task-moblin-tools-testapps = "\
    blktool \
    tslib-calibrate \
    tslib-tests \
    lrzsz \
    kexec-tools \
    alsa-utils-amixer \
    alsa-utils-aplay \
    owl-video \
    gst-meta-video \
    gst-meta-audio \
    xrestop \
    xwininfo \
    xprop \
    xvideo-tests"
