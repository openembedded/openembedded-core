DESCRIPTION = "utils for managing LZMA compressed files"
HOMEPAGE = "http://tukaani.org/xz/"
SECTION = "base"

LICENSE = "GPLv2+ & GPLv3+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=c475b6c7dca236740ace4bba553e8e1c \
                    file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://lib/getopt.c;endline=23;md5=2069b0ee710572c03bb3114e4532cd84 "

SRC_URI = "http://tukaani.org/xz/xz-${PV}.tar.gz"

SRC_URI[md5sum] = "bb24436fa12780808e1c142980484104"
SRC_URI[sha256sum] = "54e59a83690a4a0ec88a7d7c3bdef90c6b196c892a93463863c71c24fe87951a"

PR = "r0"

inherit autotools gettext

PACKAGES =+ "liblzma liblzma-dev liblzma-staticdev liblzma-dbg"

FILES_liblzma = "${libdir}/liblzma*${SOLIBS}"
FILES_liblzma-dev = "${includedir}/lzma* ${libdir}/liblzma*${SOLIBSDEV} ${libdir}/liblzma.la ${libdir}/pkgconfig/liblzma.pc"
FILES_liblzma-staticdev = "${libdir}/liblzma.a"
FILES_liblzma-dbg = "${libdir}/.debug/liblzma*"

BBCLASSEXTEND = "native"
