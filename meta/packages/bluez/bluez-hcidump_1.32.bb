DESCRIPTION = "Linux Bluetooth Stack HCI Debugger Tool."
SECTION = "console"
PRIORITY = "optional"
DEPENDS = "bluez-libs"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://bluez.sourceforge.net/download/bluez-hcidump-${PV}.tar.gz"
S = "${WORKDIR}/bluez-hcidump-${PV}"

EXTRA_OECONF = "--with-bluez-libs=${STAGING_LIBDIR} --with-bluez-includes=${STAGING_INCDIR}"

inherit autotools
