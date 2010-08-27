require linux-omap.inc

PR = "r6"

COMPATIBLE_MACHINE = "omap-3430ldp|omap-3430sdp"
DEFAULT_PREFERENCE = "1"

KERNEL_OUTPUT = "arch/${ARCH}/boot/${KERNEL_IMAGETYPE}"

#
# Patch extracted from:
# http://omapzoom.org/gf/download/frsrelease/110/425/linux-ldp-v1.4.tar
#

SRC_URI="http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.24.tar.bz2 \
         http://kernel.org/pub/linux/kernel/v2.6/patch-2.6.24.7.bz2;patch=1 \
         file://linux-2.6.24.7-ldp-v1.4.patch.gz;patch=1 \
         file://fixes.patch;patch=1 \
         file://fixes2.patch;patch=1 \
         file://module_fix.patch;patch=1 \
         file://time-prevent-the-loop-in-timespec_add_ns-from-being-optimised-away.patch;patch=1 \
         file://defconfig-${MACHINE}"

S = "${WORKDIR}/linux-2.6.24"

