#
# Copyright (C) 2007 OpenedHand Ltd
#

SUMMARY = "Host packages for the standalone SDK or external toolchain"
PR = "r11"
LICENSE = "MIT"

inherit nativesdk packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} = "\
    nativesdk-pkgconfig \
    nativesdk-qemu \
    nativesdk-qemu-helper \
    nativesdk-pseudo \
    nativesdk-unfs-server \
    nativesdk-opkg \
    nativesdk-libtool \
    nativesdk-autoconf \
    nativesdk-automake \
    "

RDEPENDS_${PN}_darwin8 = "\
    odcctools-cross-canadian \
    llvm-cross-canadian \
    nativesdk-pkgconfig \
    nativesdk-opkg \
    nativesdk-libtool \
    "
