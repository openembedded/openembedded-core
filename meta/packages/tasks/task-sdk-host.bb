#
# Copyright (C) 2007 OpenedHand Ltd
#

DESCRIPTION = "Host packages for the standalone SDK or external toolchain"
PR = "r7"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

inherit nativesdk

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    binutils-cross-nativesdk \
    gcc-cross-nativesdk \
    gdb-cross-nativesdk \
    pkgconfig-nativesdk \
    qemu-nativesdk \
    qemu-helper-nativesdk \
    opkg-nativesdk \
    "

RDEPENDS_${PN}_darwin8 = "\
    odcctools-cross-nativesdk \
    llvm-cross-nativesdk \
    pkgconfig-nativesdk \
    opkg-nativesdk \
    libtool-nativesdk \
    "
