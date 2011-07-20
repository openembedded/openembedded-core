DESCRIPTION = "GNU libiconv - libiconv is for you if your application needs to support \
multiple character encodings, but that support lacks from your system."
HOMEPAGE = "http://www.gnu.org/software/libiconv"
SECTION = "libs"
NOTES = "Needs to be stripped down to: ascii iso8859-1 eucjp iso-2022jp gb utf8"
PROVIDES = "virtual/libiconv"
PR = "r0"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674 \
                    file://libcharset/COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674"

SRC_URI = "${GNU_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz \
           file://autoconf.patch \
	   file://shared_preloadable_libiconv_linux.patch \
          "

SRC_URI[md5sum] = "7ab33ebd26687c744a37264a330bbe9a"
SRC_URI[sha256sum] = "55a36168306089009d054ccdd9d013041bfc3ab26be7033d107821f1c4949a49"

S = "${WORKDIR}/libiconv-${PV}"

inherit autotools pkgconfig gettext

EXTRA_OECONF += "--enable-shared --enable-static --enable-relocatable"

LEAD_SONAME = "libiconv.so"

do_configure_prepend () {
	rm -f m4/libtool.m4 libcharset/m4/libtool.m4
}
