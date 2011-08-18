SUMMARY = "Standard GNU compressor"
DESCRIPTION = "GNU Gzip is a popular data compression program originally written by Jean-loup Gailly for the GNU \
project. Mark Adler wrote the decompression part"
HOMEPAGE = "http://www.gnu.org/software/gzip"
SECTION = "base"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://gzip.h;endline=22;md5=c0934ad1900d927f86556153d4c76d23 \
                    file://lzw.h;endline=19;md5=c273e09a02edd9801cc74d39683049e9 "

SRC_URI = "${GNU_MIRROR}/gzip/gzip-${PV}.tar.gz \
           file://m4-extensions-fix.patch \
           file://dup-def-fix.patch"

PR = "r0"

inherit autotools

do_install () {
	autotools_do_install
	# move files into /bin (FHS)
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/gunzip ${D}${base_bindir}/gunzip.${PN}
	mv ${D}${bindir}/gzip ${D}${base_bindir}/gzip.${PN}
	mv ${D}${bindir}/zcat ${D}${base_bindir}/zcat.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_bindir}/gunzip gunzip gunzip.${PN} 100
	update-alternatives --install ${base_bindir}/gzip gzip gzip.${PN} 100
	update-alternatives --install ${base_bindir}/zcat zcat zcat.${PN} 100
}

pkg_prerm_${PN} () {
	update-alternatives --remove gunzip gunzip.${PN}
	update-alternatives --remove gzip gzip.${PN}
	update-alternatives --remove zcat zcat.${PN}
}

BBCLASSEXTEND = "native"
