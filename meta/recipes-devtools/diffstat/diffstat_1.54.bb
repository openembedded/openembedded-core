SUMMARY = "Tool to produce a statistics based on a diff"
DESCRIPTION = "diffstat reads the output of diff and displays a histogram of \
the insertions, deletions, and modifications per-file. It is useful for \
reviewing large, complex patch files."
HOMEPAGE = "http://invisible-island.net/diffstat/"
PRIORITY = "optional"
SECTION = "devel"
DEPENDS = "gettext"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://install-sh;endline=42;md5=b3549726c1022bee09c174c72a0ca4a5"
PR = "r0"

SRC_URI = "ftp://invisible-island.net/diffstat/diffstat-${PV}.tgz"

SRC_URI[md5sum] = "af08bef2eb37050ceb0c4fddedb2ee36"
SRC_URI[sha256sum] = "d8e67660ec85be597f8548ecdd088926639dac34ec7184aaf9d09c1e6ecb83e5"

S = "${WORKDIR}/diffstat-${PV}"

inherit autotools

do_configure () {
	if [ ! -e acinclude.m4 ]; then
		mv aclocal.m4 acinclude.m4
	fi
	autotools_do_configure
}
