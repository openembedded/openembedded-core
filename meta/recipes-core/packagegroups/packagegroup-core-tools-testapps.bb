#
# Copyright (C) 2008 OpenedHand Ltd.
#

SUMMARY = "Testing tools/applications"
LICENSE = "MIT"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

# kexec-tools doesn't work on Mips
KEXECTOOLS ?= "kexec"
KEXECTOOLS_mips ?= ""
KEXECTOOLS_mipsel ?= ""
KEXECTOOLS_powerpc ?= ""
KEXECTOOLS_e5500-64b ?= ""

RDEPENDS_${PN} = "\
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
    clutter-box2d \
    ltp \
    "
