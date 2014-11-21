SUMMARY = "Standard GNU compressor"
DESCRIPTION = "GNU Gzip is a popular data compression program originally written by Jean-loup Gailly for the GNU \
project. Mark Adler wrote the decompression part"
HOMEPAGE = "http://www.gnu.org/software/gzip/"
SECTION = "console/utils"
# change to GPLv3+ in 2007/07. Previous GPLv2 version is 1.3.12
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://gzip.h;beginline=8;endline=20;md5=6e47caaa630e0c8bf9f1bc8d94a8ed0e"

SRC_URI = "${GNU_MIRROR}/gzip/${BP}.tar.gz"
SRC_URI_append_class-target = " file://wrong-path-fix.patch"

inherit autotools texinfo

EXTRA_OEMAKE_class-target = "GREP=${base_bindir}/grep"

do_install_append () {
	# Rename and move files into /bin (FHS), which is typical place for gzip
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/gunzip ${D}${base_bindir}/gunzip
	mv ${D}${bindir}/gzip ${D}${base_bindir}/gzip
	mv ${D}${bindir}/zcat ${D}${base_bindir}/zcat
	mv ${D}${bindir}/uncompress ${D}${base_bindir}/uncompress
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "gunzip gzip zcat"
ALTERNATIVE_LINK_NAME[gunzip] = "${base_bindir}/gunzip"
ALTERNATIVE_LINK_NAME[gzip] = "${base_bindir}/gzip"
ALTERNATIVE_LINK_NAME[zcat] = "${base_bindir}/zcat"

export CONFIG_SHELL="/bin/sh"
