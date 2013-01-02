require mpfr.inc
LICENSE="GPLv3&LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"
DEPENDS = "gmp"
PR = "r0"

SRC_URI = "http://www.mpfr.org/mpfr-${PV}/mpfr-${PV}.tar.bz2 \
           file://long-long-thumb.patch \
           "

SRC_URI[md5sum] = "e90e0075bb1b5f626c6e31aaa9c64e3b"
SRC_URI[sha256sum] = "7b66c3f13dc8385f08264c805853f3e1a8eedab8071d582f3e661971c9acd5fd"
S = "${WORKDIR}/mpfr-${PV}"

BBCLASSEXTEND = "native nativesdk"
