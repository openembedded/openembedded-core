DESCRIPTION = "utils for managing LZMA compressed files"
HOMEPAGE = "http://tukaani.org/xz/"
SECTION = "base"

LICENSE = "GPLv2+ & GPLv3+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=c475b6c7dca236740ace4bba553e8e1c \
                    file://COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=fbc093901857fcd118f065f900982c24 \
                    file://lib/getopt.c;endline=23;md5=2069b0ee710572c03bb3114e4532cd84 "

SRC_URI = "http://tukaani.org/xz/xz-${PV}.tar.gz"

SRC_URI[md5sum] = "fefe52f9ecd521de2a8ce38c21a27574"
SRC_URI[sha256sum] = "10eb4df72dffb2fb14c3d2d82b450e72282ffcb9ee3908a8e5b392b8f09681bf"

PR = "r1"

inherit autotools gettext

PACKAGES =+ "liblzma liblzma-dev liblzma-staticdev liblzma-dbg"

FILES_liblzma = "${libdir}/liblzma*${SOLIBS}"
FILES_liblzma-dev = "${includedir}/lzma* ${libdir}/liblzma*${SOLIBSDEV} ${libdir}/liblzma.la ${libdir}/pkgconfig/liblzma.pc"
FILES_liblzma-staticdev = "${libdir}/liblzma.a"
FILES_liblzma-dbg = "${libdir}/.debug/liblzma*"

BBCLASSEXTEND = "native"
