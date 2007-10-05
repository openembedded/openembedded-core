require linux-mx31.inc

PR = "r1"

FILESDIR = "${WORKDIR}"

SRC_URI = " \
	   http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.19.2.tar.bz2 \
           file://linux-2.6.19.2-mx3lite.patch.gz \
           file://linux-2.6.19.2-mx3lite.patch;patch=1 \
           file://defconfig-mx31litekit \
	   "

SRC_URI_append_mx31litekit = " \
           file://mx31lite-boot.patch;patch=1 \
           file://mx31lite-fb.patch;patch=1 \
           file://mx31lite-spi.patch;patch=1 \
           "

S = "${WORKDIR}/linux-2.6.19.2"
