DESCRIPTION = "Very high-quality data compression program."
HOMEPAGE = "http://www.bzip.org/"
SECTION = "console/utils"
LICENSE = "bzip2"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=8;endline=37;md5=40d9d1eb05736d1bfc86cfdd9106e6b2"
PR = "r3"

SRC_URI = "http://www.bzip.org/${PV}/${BPN}-${PV}.tar.gz \
           file://configure.ac \
	   file://Makefile.am"

CFLAGS_append = " -fPIC -fpic -Winline -fno-strength-reduce -D_FILE_OFFSET_BITS=64"

inherit autotools update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_LINKS = "${bindir}/bunzip2 ${bindir}/bzcat"

do_configure_prepend () {
	cp ${WORKDIR}/configure.ac ${S}/
	cp ${WORKDIR}/Makefile.am ${S}/
	cp ${STAGING_DATADIR_NATIVE}/automake*/install-sh ${S}/
}

PROVIDES_append_virtclass-native = " bzip2-full-native"
BBCLASSEXTEND = "native"
