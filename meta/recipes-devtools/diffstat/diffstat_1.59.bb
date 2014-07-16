SUMMARY = "Tool to produce a statistics based on a diff"
DESCRIPTION = "diffstat reads the output of diff and displays a histogram of \
the insertions, deletions, and modifications per-file. It is useful for \
reviewing large, complex patch files."
HOMEPAGE = "http://invisible-island.net/diffstat/"
SECTION = "devel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://install-sh;endline=42;md5=b3549726c1022bee09c174c72a0ca4a5"

SRC_URI = "ftp://invisible-island.net/diffstat/diffstat-${PV}.tgz \
           file://run-ptest \
"

SRC_URI[md5sum] = "1dc7bc48ce846a0686a1af0d091ff8fb"
SRC_URI[sha256sum] = "267d1441b8889cbefbb7ca7dfd4a17f6c8bc73bc114904c74ecad945a3dbf270"

S = "${WORKDIR}/diffstat-${PV}"

inherit autotools gettext ptest

do_configure () {
	if [ ! -e ${S}/acinclude.m4 ]; then
		mv ${S}/aclocal.m4 ${S}/acinclude.m4
	fi
	autotools_do_configure
}

do_install_ptest() {
	cp -r ${S}/testing ${D}${PTEST_PATH}
}
