#
# Copyright (C) 2007 OpenedHand Ltd
#

DESCRIPTION = "Host packages for the standalone SDK or external toolchain"
PR = "r11"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit nativesdk

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    pkgconfig-nativesdk \
    qemu-nativesdk \
    qemu-helper-nativesdk \
    pseudo-nativesdk \
    unfs-server-nativesdk \
    opkg-nativesdk \
    libtool-nativesdk \
    autoconf-nativesdk \
    automake-nativesdk \
    "

RDEPENDS_${PN}_darwin8 = "\
    odcctools-cross-canadian \
    llvm-cross-canadian \
    pkgconfig-nativesdk \
    opkg-nativesdk \
    libtool-nativesdk \
    "
