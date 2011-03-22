DESCRIPTION = "GNU libiconv - libiconv is for you if your application needs to support \
multiple character encodings, but that support lacks from your system."
HOMEPAGE = "http://www.gnu.org/software/libiconv"
SECTION = "libs"
PRIORITY = "optional"
NOTES = "Needs to be stripped down to: ascii iso8859-1 eucjp iso-2022jp gb utf8"
PROVIDES = "virtual/libiconv"
PR = "r4"
LICENSE = "LGPL"
SRC_URI = "${GNU_MIRROR}/libiconv/libiconv-${PV}.tar.gz"

S = "${WORKDIR}/libiconv-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-shared --enable-static"

do_configure () {
	rm -f m4/libtool.m4 libcharset/m4/libtool.m4
	autotools_do_configure
}
