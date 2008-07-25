require linux-omap.inc
PR = "r5"
COMPATIBLE_MACHINE = "omap-3430ldp"
SRC_URI="http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.22.19.tar.bz2 \
         file://defconfig-omap-3430ldp \
         file://linux-2.6.22.19-ldp-v1.3.patch.gz;patch=1 \
	 file://fixes.patch;patch=1"
S = "${WORKDIR}/linux-2.6.22.19"

