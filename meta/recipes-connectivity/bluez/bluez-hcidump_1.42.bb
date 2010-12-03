SUMMARY = "Linux Bluetooth Stack HCI Debugger Tool."
DESCRIPTION = "The hcidump tool reads raw HCI data coming from and going to a Bluetooth device \
and displays the commands, events and data in a human-readable form."

SECTION = "console"
PRIORITY = "optional"
DEPENDS = "bluez4"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://src/hcidump.c;beginline=1;endline=23;md5=22f51010959a0cd483c4d7f0bd77aaa3"
PR = "r0"

SRC_URI = "http://bluez.sourceforge.net/download/bluez-hcidump-${PV}.tar.gz"
S = "${WORKDIR}/bluez-hcidump-${PV}"

EXTRA_OECONF = "--with-bluez-libs=${STAGING_LIBDIR} --with-bluez-includes=${STAGING_INCDIR}"

inherit autotools
