SUMMARY = "Character encoding support library"
DESCRIPTION = "GNU libiconv - libiconv is for you if your application needs to support \
multiple character encodings, but that support lacks from your system."
HOMEPAGE = "http://www.gnu.org/software/libiconv"
SECTION = "libs"
NOTES = "Needs to be stripped down to: ascii iso8859-1 eucjp iso-2022jp gb utf8"
PROVIDES = "virtual/libiconv"
LICENSE = "LGPL"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674 \
                    file://libcharset/COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674"
SRC_URI = "${GNU_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz \
           file://autoconf.patch \
	   file://shared_preloadable_libiconv_linux.patch \
          "

SRC_URI[md5sum] = "d42b97f6ef5dd0ba4469d520ed732fed"
SRC_URI[sha256sum] = "e78c347a1a0cb15f2648519e9799151f4b4a934b61ad9ee7424478efe2b8257f"

S = "${WORKDIR}/libiconv-${PV}"

inherit autotools pkgconfig gettext

python __anonymous() {
    if d.getVar("TCLIBC", True) == "glibc":
        raise bb.parse.SkipPackage("libiconv is provided for use with uClibc only - glibc already provides iconv")
}

EXTRA_OECONF += "--enable-shared --enable-static --enable-relocatable"

LEAD_SONAME = "libiconv.so"

do_configure_prepend () {
	rm -f m4/libtool.m4 libcharset/m4/libtool.m4
}
