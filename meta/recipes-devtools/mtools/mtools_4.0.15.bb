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

SRC_URI[md5sum] = "b7550b649af77812cb696a780e853f47"
SRC_URI[sha256sum] = "290defca107cc183a17c98d3f7d00db02228b724084a2a818f1dd6ea86973899"

inherit autotools

EXTRA_OECONF = "--without-x"

BBCLASSEXTEND = "native"
