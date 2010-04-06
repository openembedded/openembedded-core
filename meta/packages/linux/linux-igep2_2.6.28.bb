require linux.inc

DESCRIPTION = "Linux kernel for OMAP processors"
KERNEL_IMAGETYPE = "uImage"

COMPATIBLE_MACHINE = "igep0020"

DEFAULT_PREFERENCE_igep0020 = "1"

PR = "r4"

KV = "2.6.28.10-3"

SRC_URI = "http://downloads.myigep.com/sources/kernel/linux-omap-${KV}.tar.gz \
	   file://defconfig \
"

S = "${WORKDIR}/linux-omap-${KV}"
