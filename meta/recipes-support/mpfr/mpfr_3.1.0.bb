require mpfr.inc
LICENSE="GPLv3&LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"
DEPENDS = "gmp"
PR = "r1"

SRC_URI = "http://www.mpfr.org/mpfr-${PV}/mpfr-${PV}.tar.bz2 \
  file://long-long-thumb.patch \
"

SRC_URI[md5sum] = "238ae4a15cc3a5049b723daef5d17938"
SRC_URI[sha256sum] = "74a7bbbad168dd1cc414f1c9210b8fc16ccfc8e422d34b3371a8978e31eab680"
S = "${WORKDIR}/mpfr-${PV}"

BBCLASSEXTEND = "native nativesdk"
