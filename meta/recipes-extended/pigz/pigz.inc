SUMMARY = "A parallel implementation of gzip"
DESCRIPTION = "pigz, which stands for parallel implementation of gzip, is a \
fully functional replacement for gzip that exploits multiple processors and \
multiple cores to the hilt when compressing data. pigz was written by Mark \
Adler, and uses the zlib and pthread libraries."
HOMEPAGE = "http://zlib.net/pigz/"
SECTION = "console/utils"
LICENSE = "Zlib"

SRC_URI = "http://zlib.net/pigz/pigz-${PV}.tar.gz \
           file://ldflags.patch"

PROVIDES_virtclass-native += "gzip-native"

DEPENDS = "zlib"

do_install () {
        install -d ${D}${bindir}
        install ${B}/pigz ${D}${bindir}/gzip
        install ${B}/unpigz ${D}${bindir}/gunzip
}
