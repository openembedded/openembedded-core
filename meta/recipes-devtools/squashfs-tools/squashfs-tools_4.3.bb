# Note, we can probably remove the lzma option as it has be replaced with xz,
# and I don't think the kernel supports it any more.
SUMMARY = "Tools for manipulating SquashFS filesystems"
SECTION = "base"
LICENSE = "GPL-2 & PD"
LIC_FILES_CHKSUM = "file://../COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://../../7zC.txt;beginline=12;endline=16;md5=2056cd6d919ebc3807602143c7449a7c \
                   "
DEPENDS = "attr zlib xz lzo lz4"

SRC_URI = "${SOURCEFORGE_MIRROR}/squashfs/squashfs${PV}.tar.gz;name=squashfs \
           http://downloads.sourceforge.net/sevenzip/lzma465.tar.bz2;name=lzma \
          "

SRC_URI[squashfs.md5sum] = "d92ab59aabf5173f2a59089531e30dbf"
SRC_URI[squashfs.sha256sum] = "0d605512437b1eb800b4736791559295ee5f60177e102e4d4ccd0ee241a5f3f6"

SRC_URI[lzma.md5sum] = "29d5ffd03a5a3e51aef6a74e9eafb759"
SRC_URI[lzma.sha256sum] = "c935fd04dd8e0e8c688a3078f3675d699679a90be81c12686837e0880aa0fa1e"

S = "${WORKDIR}/squashfs${PV}/squashfs-tools"
SPDX_S = "${WORKDIR}/squashfs${PV}"

# EXTRA_OEMAKE is typically: -e MAKEFLAGS=
# the -e causes problems as CFLAGS is modified in the Makefile, so
# we redefine EXTRA_OEMAKE here
EXTRA_OEMAKE = "MAKEFLAGS= LZMA_SUPPORT=1 LZMA_DIR=../.. XZ_SUPPORT=1 LZO_SUPPORT=1 LZ4_SUPPORT=1"

do_compile() {
	oe_runmake mksquashfs unsquashfs
}
do_install () {
        install -d ${D}${sbindir}
        install -m 0755 mksquashfs ${D}${sbindir}/
	install -m 0755 unsquashfs ${D}${sbindir}/
}

ARM_INSTRUCTION_SET = "arm"

BBCLASSEXTEND = "native nativesdk"
