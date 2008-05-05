#
# Copyright (C) 2007 OpenedHand Ltd
#

DESCRIPTION = "Host packages for the standalone SDK or external toolchain"
PR = "r7"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

inherit sdk

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    binutils-cross-sdk \
    gcc-cross-sdk \
    gdb-cross-sdk \
    pkgconfig-sdk \
    qemu-sdk \
    qemu-helper-sdk \
    opkg-sdk \
    "

RDEPENDS_${PN}_darwin8 = "\
    odcctools-cross-sdk \
    llvm-cross-sdk \
    pkgconfig-sdk \
    opkg-sdk \
    libtool-sdk \
    "
