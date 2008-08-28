SECTION = "base"
DESCRIPTION = "Gives a fake root environment which can support chroot"
LICENSE = "GPL"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_${PV}.orig.tar.gz \
           file://fix-readlink.patch;patch=1"

inherit autotools
