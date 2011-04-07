require mpfr.inc
LICENSE="GPLv3&LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6"
DEPENDS = "gmp"
PR = "r0"

SRC_URI = "http://www.mpfr.org/mpfr-${PV}/mpfr-${PV}.tar.bz2"

SRC_URI[md5sum] = "bfbecb2eacb6d48432ead5cfc3f7390a"
SRC_URI[sha256sum] = "e1977099bb494319c0f0c1f85759050c418a56884e9c6cef1c540b9b13e38e7f"
S = "${WORKDIR}/mpfr-${PV}"

BBCLASSEXTEND = "native nativesdk"
