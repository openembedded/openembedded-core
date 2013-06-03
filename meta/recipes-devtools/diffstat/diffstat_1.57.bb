SUMMARY = "Tool to produce a statistics based on a diff"
DESCRIPTION = "diffstat reads the output of diff and displays a histogram of \
the insertions, deletions, and modifications per-file. It is useful for \
reviewing large, complex patch files."
HOMEPAGE = "http://invisible-island.net/diffstat/"
SECTION = "devel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://install-sh;endline=42;md5=b3549726c1022bee09c174c72a0ca4a5"

SRC_URI = "ftp://invisible-island.net/diffstat/diffstat-${PV}.tgz \
           file://dirfix.patch \
           file://aclocal.patch"

SRC_URI[md5sum] = "a70ae35e479ab91da7eb6023a4e9240a"
SRC_URI[sha256sum] = "cb9845839d695f178d6b5458b08d3e04773e400f35c0c062c4c0102220fba1e6"

S = "${WORKDIR}/diffstat-${PV}"

inherit autotools gettext

do_configure () {
	if [ ! -e ${S}/acinclude.m4 ]; then
		mv ${S}/aclocal.m4 ${S}/acinclude.m4
	fi
	autotools_do_configure
}
