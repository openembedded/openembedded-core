require linux-omap.inc

COMPATIBLE_MACHINE = "omap-zoom"

PR = "r0"
FILESDIR = "${WORKDIR}"

SRC_URI = "http://www.omapzoom.org/pub/kernel/linux-ldp-v1.2.tar.gz \
           file://defconfig-omap-zoom"

S = "${WORKDIR}/linux-ldp-v1.2"
