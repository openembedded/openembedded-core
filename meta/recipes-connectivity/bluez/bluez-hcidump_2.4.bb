SUMMARY = "Linux Bluetooth Stack HCI Debugger Tool."
DESCRIPTION = "The hcidump tool reads raw HCI data coming from and going to a Bluetooth device \
and displays the commands, events and data in a human-readable form."

SECTION = "console"
DEPENDS = "bluez4"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://src/hcidump.c;beginline=1;endline=23;md5=3bee3a162dff43a5be7470710b99fbcf"
PR = "r0"

SRC_URI = "http://www.kernel.org/pub/linux/bluetooth/bluez-hcidump-${PV}.tar.gz"

SRC_URI[md5sum] = "d39833de0ed26bddf784e9580b6f33c4"
SRC_URI[sha256sum] = "bacd155e6420e480d8a30c0b0e6da990031890ee1000a31408e175ab7cf3cbd3"
S = "${WORKDIR}/bluez-hcidump-${PV}"

EXTRA_OECONF = "--with-bluez-libs=${STAGING_LIBDIR} --with-bluez-includes=${STAGING_INCDIR}"

inherit autotools
