require linux-simtec.inc

PR = "r1"

PROVIDES += "virtual/kernel"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_depicture = "1"

COMPATIBLE_MACHINE = "depicture"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-2.6.26.tar.bz2 \
           http://www.simtec.co.uk/products/SWLINUX/files/patch-2.6.26-simtec3.bz2;patch=1 \
	   file://${MACHINE}-defconfig-append \
	   "

S = "${WORKDIR}/linux-2.6.26"
