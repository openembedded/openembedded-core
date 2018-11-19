SUMMARY = "General-purpose x86 assembler"
SECTION = "devel"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=90904486f8fbf1861cf42752e1a39efe"

SRC_URI = "http://www.nasm.us/pub/nasm/releasebuilds/${PV}/nasm-${PV}.tar.bz2"

SRC_URI[md5sum] = "238a240d3f869a52f8ac38ee3f8faafa"
SRC_URI[sha256sum] = "d43cfd27cad53d0c22a9bf9702e9dffcc7018a0df21d15b92c56d250d747c744"

# brokensep since this uses autoconf but not automake
inherit autotools-brokensep

EXTRA_AUTORECONF += "--exclude=aclocal"

BBCLASSEXTEND = "native"

DEPENDS = "groff-native"
