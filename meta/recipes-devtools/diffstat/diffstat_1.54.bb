DESCRIPTION = "diffstat reads the output of diff and displays a histogram of \
the insertions, deletions, and modifications per-file. It is useful for \
reviewing large, complex patch files."
HOMEPAGE = "http://invisible-island.net/diffstat/"
PRIORITY = "optional"
SECTION = "devel"
DEPENDS = "gettext"
LICENSE = "MIT"
PR = "r0"

SRC_URI = "ftp://invisible-island.net/diffstat/diffstat-${PV}.tgz"

S = "${WORKDIR}/diffstat-${PV}"

inherit autotools

do_configure () {
	if [ ! -e acinclude.m4 ]; then
		mv aclocal.m4 acinclude.m4
	fi
	autotools_do_configure
}
