#
# Copyright (C) 2008 OpenedHand Ltd.
#

SUMMARY = "Testing tools/applications"
LICENSE = "MIT"

PR = "r2"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-tools-testapps"
RREPLACES_${PN} = "task-core-tools-testapps"
RCONFLICTS_${PN} = "task-core-tools-testapps"

# kexec-tools doesn't work on Mips
KEXECTOOLS ?= "kexec"
KEXECTOOLS_mips ?= ""
KEXECTOOLS_mipsel ?= ""
KEXECTOOLS_powerpc ?= ""
KEXECTOOLS_e5500-64b ?= ""
KEXECTOOLS_aarch64 ?= ""

X11GLTOOLS = "\
    mesa-demos \
    piglit \
    "

3GTOOLS = "\
    ofono-tests \
    "

X11TOOLS = "\
    fstests \
    owl-video \
    x11perf \
    xrestop \
    xwininfo \
    xprop \
    xvideo-tests \
    "

RDEPENDS_${PN} = "\
    blktool \
    tslib-calibrate \
    tslib-tests \
    lrzsz \
    ${KEXECTOOLS} \
    alsa-utils-amixer \
    alsa-utils-aplay \
    gst-meta-video \
    gst-meta-audio \
    ltp \
    connman-tools \
    connman-tests \
    connman-client \
    ${@base_contains('DISTRO_FEATURES', 'x11', "${X11TOOLS}", "", d)} \
    ${@base_contains('DISTRO_FEATURES', 'x11 opengl', "${X11GLTOOLS}", "", d)} \
    ${@base_contains('DISTRO_FEATURES', '3g', "${3GTOOLS}", "", d)} \
    "
