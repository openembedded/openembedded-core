SUMMARY = "Utilities to access MS-DOS disks without mounting them"
DESCRIPTION = "Mtools is a collection of utilities to access MS-DOS disks from GNU and Unix without mounting them."
HOMEPAGE = "http://www.gnu.org/software/mtools/"
SECTION = "optional"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PR = "r0"

SRC_URI = "${GNU_MIRROR}/mtools/mtools-${PV}.tar.bz2 \
           file://mtools-makeinfo.patch \
           file://no-x11.gplv3.patch"

SRC_URI[md5sum] = "15571c615d8f75f5f6d294272f80c7fa"
SRC_URI[sha256sum] = "0ecc358e30a72d215b1d4c625b27e67121cd6f6075370dfb791ef2a8b980ecb6"

inherit autotools

EXTRA_OECONF = "--without-x"

PARALLEL_MAKEINST = ""

BBCLASSEXTEND = "native"
