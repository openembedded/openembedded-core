require mpfr.inc

LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"
DEPENDS = "gmp"

SRC_URI = "http://www.mpfr.org/mpfr-${PV}/mpfr-${PV}.tar.xz \
           file://long-long-thumb.patch \
           "

SRC_URI[md5sum] = "e3d203d188b8fe60bb6578dd3152e05c"
SRC_URI[sha256sum] = "399d0f47ef6608cc01d29ed1b99c7faff36d9994c45f36f41ba250147100453b"

S = "${WORKDIR}/mpfr-${PV}"

BBCLASSEXTEND = "native nativesdk"
