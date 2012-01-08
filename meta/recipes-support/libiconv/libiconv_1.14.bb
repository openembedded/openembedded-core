DESCRIPTION = "GNU libiconv - libiconv is for you if your application needs to support \
multiple character encodings, but that support lacks from your system."
HOMEPAGE = "http://www.gnu.org/software/libiconv"
SECTION = "libs"
NOTES = "Needs to be stripped down to: ascii iso8859-1 eucjp iso-2022jp gb utf8"
PROVIDES = "virtual/libiconv"
PR = "r1"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674 \
                    file://libcharset/COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674"

SRC_URI = "${GNU_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz \
           file://autoconf.patch \
           file://add-relocatable-module.patch \
          "

SRC_URI[md5sum] = "e34509b1623cec449dfeb73d7ce9c6c6"
SRC_URI[sha256sum] = "72b24ded17d687193c3366d0ebe7cde1e6b18f0df8c55438ac95be39e8a30613"

S = "${WORKDIR}/libiconv-${PV}"

inherit autotools pkgconfig gettext

EXTRA_OECONF += "--enable-shared --enable-static --enable-relocatable"

LEAD_SONAME = "libiconv.so"

do_configure_prepend () {
	rm -f m4/libtool.m4 m4/ltoptions.m4 m4/ltsugar.m4 m4/ltversion.m4 m4/lt~obsolete.m4 libcharset/m4/libtool.m4 libcharset/m4/ltoptions.m4 libcharset/m4/ltsugar.m4 libcharset/m4/ltversion.m4 libcharset/m4/lt~obsolete.m4
}

do_install_append () {
	rm -rf ${D}${libdir}/preloadable_libiconv.so
	rm -rf ${D}${libdir}/charset.alias
}
