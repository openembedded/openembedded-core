DESCRIPTION = "GNU libiconv - libiconv is for you if your application needs to support \
multiple character encodings, but that support lacks from your system."
HOMEPAGE = "http://www.gnu.org/software/libiconv"
SECTION = "e/libs"
PRIORITY = "optional"
MAINTAINER = "That Crazy fool emte <emte@labotomy.net>"
NOTES = "Needs to be stripped down to: ascii iso8859-1 eucjp iso-2022jp gb utf8"
PROVIDES = "virtual/libiconv"
PR = "r3"
LICENSE = "LGPL"
SRC_URI = "ftp://ftp.gnu.org/pub/gnu/libiconv/libiconv-${PV}.tar.gz"

S = "${WORKDIR}/libiconv-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-shared --enable-static"

do_configure () {
	rm -f m4/libtool.m4 libcharset/m4/libtool.m4
	autotools_do_configure
}

do_stage () {
	oe_libinstall -so -a -C lib libiconv ${STAGING_LIBDIR}
	oe_libinstall -so -C lib libiconv_plug_linux ${STAGING_LIBDIR}
	oe_libinstall -so -a -C libcharset/lib libcharset ${STAGING_LIBDIR}
	autotools_stage_includes
}
