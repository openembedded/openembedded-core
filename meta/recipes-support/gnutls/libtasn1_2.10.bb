DESCRIPTION = "Library for ASN.1 and DER manipulation"
HOMEPAGE = "http://www.gnu.org/software/libtasn1/"

LICENSE = "GPLv3+ & LGPLv2.1+"
LICENSE_${PN}-bin = "GPLv3+"
LICENSE_${PN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
                    file://README;endline=8;md5=c3803a3e8ca5ab5eb1e5912faa405351"

SRC_URI[md5sum] = "ef80c227d0dcdc2940fbc58faf8e0cf1"
SRC_URI[sha256sum] = "17e4c06212e00437c13633c26ed73dad21784805008cf7550f1399adbd1006d0"

PR = "r0"

SRC_URI = "ftp://ftp.gnu.org/gnu/libtasn1/libtasn1-${PV}.tar.gz"

inherit autotools binconfig lib_package

BBCLASSEXTEND = "native"
