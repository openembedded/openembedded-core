SECTION = "base"
DESCRIPTION = "Gives a fake root environment which can support chroot"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_${PV}.orig.tar.gz;name=tarball \
           file://fix-readlink.patch;patch=1 \
           ${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_2.9-1.1.diff.gz;patch=1;name=patch"

SRC_URI[tarball.md5sum] = "7b0048e50a6309acf0dda4e547735022"
SRC_URI[tarball.sha256sum] = "bfccb079d2d247b05d5f82473beca0043ddfc51a6ed595541575893fa3ab6c3e"

SRC_URI[patch.md5sum] = "b4c9c113ebba929be8c311591a499e6e"
SRC_URI[patch.sha256sum] = "3e1677622f3a92a5cf91665a733fa51b32d1e5f1a487a7e220ad575c0a09077a"

inherit autotools

PR = "r3"

BBCLASSEXTEND = "native"
