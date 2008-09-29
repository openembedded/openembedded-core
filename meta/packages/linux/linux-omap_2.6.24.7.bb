require linux-omap.inc

PR = "r2"

COMPATIBLE_MACHINE = "omap-3430ldp"

#
# Patch extracted from:
# http://omapzoom.org/gf/download/frsrelease/110/425/linux-ldp-v1.4.tar
#

SRC_URI="http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.24.tar.bz2 \
         http://kernel.org/pub/linux/kernel/v2.6/patch-2.6.24.7.bz2;patch=1 \
         file://linux-2.6.24.7-ldp-v1.4.patch.gz;patch=1 \
         file://fixes.patch;patch=1 \
         file://module_fix.patch;patch=1 \
         file://defconfig-omap-3430ldp"

S = "${WORKDIR}/linux-2.6.24"

