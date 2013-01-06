DESCRIPTION = "Library for ASN.1 and DER manipulation"
HOMEPAGE = "http://www.gnu.org/software/libtasn1/"

LICENSE = "GPLv3+ & LGPLv2.1+"
LICENSE_${PN}-bin = "GPLv3+"
LICENSE_${PN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
                    file://README;endline=8;md5=c3803a3e8ca5ab5eb1e5912faa405351"

PR = "r1"

SRC_URI = "${GNU_MIRROR}/libtasn1/libtasn1-${PV}.tar.gz \
           file://libtasn1_fix_for_automake_1.12.patch \
           file://dont-depend-on-help2man.patch \
           "

SRC_URI[md5sum] = "a08c5936f9672da9b05691785499bcc3"
SRC_URI[sha256sum] = "bc2936cd20267859278145e563427c274d27aaae30ecdf50a04cdd4ec0153d54"

inherit autotools binconfig lib_package

BBCLASSEXTEND = "native"
