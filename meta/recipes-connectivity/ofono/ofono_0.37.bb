require ofono.inc

PR = "r0"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/ofono/${P}.tar.bz2 \
	          file://ofono"

SRC_URI[md5sum] = "fa9a6f91506db2ac5bf313ff0bde65f3"
SRC_URI[sha256sum] = "0ac71d8a846d564ef6940c47bcc410b76df750aadf7903cb56876d991275f6db"
