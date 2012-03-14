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

SRC_URI[md5sum] = "0cc8ff247d05010e87e9125b31d335ff"
SRC_URI[sha256sum] = "61c7e7dcad6ed8fce5f4bcc4a0f0d9a83c36642da404348388e6411d6f78d227"
S = "${WORKDIR}/bluez-hcidump-${PV}"

EXTRA_OECONF = "--with-bluez-libs=${STAGING_LIBDIR} --with-bluez-includes=${STAGING_INCDIR}"

inherit autotools
