# Note, we can probably remove the lzma option as it has be replaced with xz,
# and I don't think the kernel supports it any more.
DESCRIPTION = "Tools to manipulate Squashfs filesystems."
SECTION = "base"
LICENSE = "GPL-2 & PD"
LIC_FILES_CHKSUM = "file://../COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://../../7zC.txt;beginline=12;endline=16;md5=2056cd6d919ebc3807602143c7449a7c \
                   "
DEPENDS = "attr zlib xz"
PR = "1"

SRC_URI = "${SOURCEFORGE_MIRROR}/squashfs/squashfs${PV}.tar.gz;name=squashfs \
           http://downloads.sourceforge.net/sevenzip/lzma465.tar.bz2;name=lzma \
          "
SRC_URI[squashfs.md5sum] = "1b7a781fb4cf8938842279bd3e8ee852"
SRC_URI[squashfs.sha256sum] = "d9e0195aa922dbb665ed322b9aaa96e04a476ee650f39bbeadb0d00b24022e96"
SRC_URI[lzma.md5sum] = "29d5ffd03a5a3e51aef6a74e9eafb759"
SRC_URI[lzma.sha256sum] = "c935fd04dd8e0e8c688a3078f3675d699679a90be81c12686837e0880aa0fa1e"

S = "${WORKDIR}/squashfs${PV}/squashfs-tools"

# EXTRA_OEMAKE is typically: -e MAKEFLAGS=
# the -e causes problems as CFLAGS is modified in the Makefile, so
# we redefine EXTRA_OEMAKE here
EXTRA_OEMAKE = "MAKEFLAGS= LZMA_SUPPORT=1 LZMA_DIR=../.. XZ_SUPPORT=1"

do_compile() {
        oe_runmake mksquashfs
}
do_install () {
        install -d ${D}${sbindir}
        install -m 0755 mksquashfs ${D}${sbindir}/
}

# required to share same place with -lzma specific packages
FILESPATHPKG =. "squashfs-tools-${PV}:"

ARM_INSTRUCTION_SET = "arm"

BBCLASSEXTEND = "native"
