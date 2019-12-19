SUMMARY = "Library for ASN.1 and DER manipulation"
HOMEPAGE = "http://www.gnu.org/software/libtasn1/"

LICENSE = "GPLv3+ & LGPLv2.1+"
LICENSE_${PN}-bin = "GPLv3+"
LICENSE_${PN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://doc/COPYING.LESSER;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE;md5=75ac100ec923f959898182307970c360"

SRC_URI = "${GNU_MIRROR}/libtasn1/libtasn1-${PV}.tar.gz \
           file://dont-depend-on-help2man.patch \
           "

DEPENDS = "bison-native"

SRC_URI[md5sum] = "33e3fb5501bb2142184238c815b0beb8"
SRC_URI[sha256sum] = "dd77509fe8f5304deafbca654dc7f0ea57f5841f41ba530cff9a5bf71382739e"

inherit autotools texinfo lib_package gtk-doc

CFLAGS_append_class-native = " -std=gnu99"

BBCLASSEXTEND = "native nativesdk"
