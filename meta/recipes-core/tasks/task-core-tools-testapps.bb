#
# Copyright (C) 2008 OpenedHand Ltd.
#

DESCRIPTION = "Test apps task for OE-Core"
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

# kexec-tools doesn't work on Mips
KEXECTOOLS ?= "kexec"
KEXECTOOLS_mips ?= ""
KEXECTOOLS_mipsel ?= ""
KEXECTOOLS_powerpc ?= ""

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
