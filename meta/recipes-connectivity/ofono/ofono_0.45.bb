require ofono.inc

PR = "r0"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/ofono/${P}.tar.bz2 \
	          file://ofono"

SRC_URI[md5sum] = "31dabb077e7592ba36913bd9d0c76b94"
SRC_URI[sha256sum] = "5541e832fb72c6c647b663e5ee55384a33efaee5a34c4544e3a16af94892f47e"
