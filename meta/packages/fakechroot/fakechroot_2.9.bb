SECTION = "base"
DESCRIPTION = "Gives a fake root environment which can support chroot"
LICENSE = "GPL"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_${PV}.orig.tar.gz \
           file://fix-readlink.patch;patch=1 \
           ${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_2.9-1.1.diff.gz"

inherit autotools

PR = "r2"

BBCLASSEXTEND = "native"