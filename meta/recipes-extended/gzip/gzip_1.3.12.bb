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

SRC_URI[md5sum] = "b5bac2d21840ae077e0217bc5e4845b1"
SRC_URI[sha256sum] = "3f565be05f7f3d1aff117c030eb7c738300510b7d098cedea796ca8e4cd587af"

PR = "r2"

inherit autotools

do_install_append () {
	# move files into /bin (FHS)
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/gunzip ${D}${base_bindir}/gunzip
	mv ${D}${bindir}/gzip ${D}${base_bindir}/gzip
	mv ${D}${bindir}/zcat ${D}${base_bindir}/zcat
	mv ${D}${bindir}/uncompress ${D}${base_bindir}/uncompress
}

inherit update-alternatives

ALTERNATIVE_${PN} = "gzip gunzip zcat"
ALTERNATIVE_LINK_NAME[gzip] = "${base_bindir}/gzip"
ALTERNATIVE_LINK_NAME[gunzip] = "${base_bindir}/gunzip"
ALTERNATIVE_LINK_NAME[zcat] = "${base_bindir}/zcat"
ALTERNATIVE_PRIORITY = "100"

BBCLASSEXTEND = "native"
