DESCRIPTION = "Sanitized set of 2.6 kernel headers for the C library's use."
SECTION = "devel"
LICENSE = "GPLv2"

LIC_CHKSUM_FILES = "file://COPYING " 
SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-${PV}.tar.bz2"

#DEPENDS = "cross-linkage"
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"
