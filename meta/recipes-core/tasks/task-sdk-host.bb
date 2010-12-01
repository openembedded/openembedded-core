#
# Copyright (C) 2007 OpenedHand Ltd
#

DESCRIPTION = "Host packages for the standalone SDK or external toolchain"
PR = "r9"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

inherit nativesdk

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    pkgconfig-nativesdk \
    qemu-nativesdk \
    qemu-helper-nativesdk \
    pseudo-nativesdk \
    unfs-server-nativesdk \
    opkg-nativesdk \
    "

RDEPENDS_${PN}_darwin8 = "\
    odcctools-cross-canadian \
    llvm-cross-canadian \
    pkgconfig-nativesdk \
    opkg-nativesdk \
    libtool-nativesdk \
    "
