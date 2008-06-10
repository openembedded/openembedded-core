require linux-omap.inc
PR = "r2"
COMPATIBLE_MACHINE = "omap-3430sdp"
SRC_URI="http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.22.19.tar.bz2 \
         file://defconfig-omap-3430sdp \
         file://linux-2.6.22.19-ldp-v1.3.patch.gz;patch=1"
S = "${WORKDIR}/linux-2.6.22.19"

