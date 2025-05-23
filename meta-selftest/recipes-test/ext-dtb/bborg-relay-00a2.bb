
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6 "

# Borrowed an example dts file from
# https://raw.githubusercontent.com/beagleboard/linux/refs/heads/5.10/arch/arm/boot/dts/overlays/BBORG_RELAY-00A2.dts
SRC_URI = "file://BBORG_RELAY-00A2.dts"

# No need for compiling the kernel. The trivial dts file does not include something.
KERNEL_INCLUDE = ""

COMPATIBLE_MACHINE = "^beaglebone-yocto$"

inherit devicetree
