SECTION = "base"
DESCRIPTION = "Gives a fake root environment which can support chroot"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_${PV}.orig.tar.gz \
           file://fix-readlink.patch;patch=1 \
           ${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_2.9-1.1.diff.gz;patch=1"

inherit autotools

PR = "r3"

BBCLASSEXTEND = "native"
