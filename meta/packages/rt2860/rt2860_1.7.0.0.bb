DESCRIPTION = "Driver for the 802.11n RaLink rt2860 chipset"
PRIORITY = "optional"
SECTION = "kernel/modules"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://folks.o-hand.com/sameo/rt2860/rt2860-1.7.0.0.tar.bz2 \
           file://01_dev_get_by_name.patch;patch=1 \
           file://02_wpa-fix.patch;patch=1" \
           file://03-iwe_stream_add.patch;patch=1" \
           file://04-pci_name.patch;patch=1"

S = "${WORKDIR}/rt2860-1.7.0.0"

COMPATIBLE_MACHINE = "eee901"

inherit module

EXTRA_OEMAKE = "'CC=${KERNEL_CC}' \
                'LD=${KERNEL_LD}' \
                'KDIR=${STAGING_KERNEL_DIR}'"

MODULES = "rt2860"

do_install() {
        install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
        install -m 0644 *${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
}
