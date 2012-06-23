DESCRIPTION = "Library for ASN.1 and DER manipulation"
HOMEPAGE = "http://www.gnu.org/software/libtasn1/"

LICENSE = "GPLv3+ & LGPLv2.1+"
LICENSE_${PN}-bin = "GPLv3+"
LICENSE_${PN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
                    file://README;endline=8;md5=c3803a3e8ca5ab5eb1e5912faa405351"

PR = "r0"

SRC_URI = "${GNU_MIRROR}/libtasn1/libtasn1-${PV}.tar.gz \
           file://libtasn1_fix_for_automake_1.12.patch \
           "

SRC_URI[md5sum] = "df27eaddcc46172377e6b907e33ddc83"
SRC_URI[sha256sum] = "a56e46483d1d42fe44ba58c9758620844042468d7c2d75f6318cdf3222745a91"

inherit autotools binconfig lib_package

BBCLASSEXTEND = "native"
