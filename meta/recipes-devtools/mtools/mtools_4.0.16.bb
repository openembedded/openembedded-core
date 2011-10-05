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

SRC_URI[md5sum] = "e9b07f35272210f407012abaf5d1b9b5"
SRC_URI[sha256sum] = "7c9e337786d142399d37202ff2002f95bfb3f4e96620223a18d7206708ad2ed5"

inherit autotools

EXTRA_OECONF = "--without-x"

PARALLEL_MAKEINST = ""

BBCLASSEXTEND = "native"
