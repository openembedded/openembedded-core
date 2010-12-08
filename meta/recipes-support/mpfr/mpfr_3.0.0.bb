require mpfr.inc
LICENSE="GPLv3&LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"
DEPENDS = "gmp"
PR = "r0"

SRC_URI = "http://www.mpfr.org/mpfr-${PV}/mpfr-${PV}.tar.bz2"

SRC_URI[md5sum] = "f45bac3584922c8004a10060ab1a8f9f"
SRC_URI[sha256sum] = "8f4e5f9c53536cb798a30455ac429b1f9fc75a0f8af32d6e0ac31ebf1024821f"
S = "${WORKDIR}/mpfr-${PV}"

BBCLASSEXTEND = "native nativesdk"
