require cryptodev_${PV}.inc

SUMMARY = "A /dev/crypto device driver kernel module"

inherit module

# Header file provided by a separate package
DEPENDS += "cryptodev-linux"

SRC_URI += " \
file://0001-Disable-installing-header-file-provided-by-another-p.patch \
file://0002-In-the-3.13-rc1-Linux-kernel-the-INIT_COMPLETION-mac.patch \
"

EXTRA_OEMAKE='KERNEL_DIR="${STAGING_KERNEL_DIR}" PREFIX="${D}"'

RCONFLICTS_${PN} = "ocf-linux"
RREPLACES_${PN} = "ocf-linux"
