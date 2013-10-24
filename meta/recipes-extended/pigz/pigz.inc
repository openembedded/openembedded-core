SUMMARY = "A parallel implementation of gzip"
DESCRIPTION = "pigz, which stands for parallel implementation of gzip, is a \
fully functional replacement for gzip that exploits multiple processors and \
multiple cores to the hilt when compressing data. pigz was written by Mark \
Adler, and uses the zlib and pthread libraries."
HOMEPAGE = "http://zlib.net/pigz/"
SECTION = "console/utils"
LICENSE = "Zlib"

SRC_URI = "http://zlib.net/pigz/pigz-${PV}.tar.gz"

PROVIDES_class-native += "gzip-native"

DEPENDS = "zlib"

do_install () {
	if [ "${CLASSOVERRIDE}" = "class-target" ] ; then
		# Install files into /bin (FHS), which is typical place for gzip
	        install -d ${D}${base_bindir}	        
		install ${B}/pigz ${D}${base_bindir}/gzip
	        install ${B}/unpigz ${D}${base_bindir}/gunzip
	else
	        install -d ${D}${bindir}
	        install ${B}/pigz ${D}${bindir}/gzip
	        install ${B}/unpigz ${D}${bindir}/gunzip
	fi
}

ALTERNATIVE_${PN} = "gzip gunzip"
ALTERNATIVE_LINK_NAME[gzip] = "${base_bindir}/gzip"
ALTERNATIVE_LINK_NAME[gunzip] = "${base_bindir}/gunzip"
ALTERNATIVE_PRIORITY = "80"
