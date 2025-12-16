SUMMARY = "Freetype font rendering library"
DESCRIPTION = "FreeType is a software font engine that is designed to be small, efficient, \
highly customizable, and portable while capable of producing high-quality output (glyph \
images). It can be used in graphics libraries, display servers, font conversion tools, text \
image generation tools, and many other products as well."
HOMEPAGE = "https://freetype.org/"
BUGTRACKER = "https://gitlab.freedesktop.org/groups/freetype/-/issues"
SECTION = "libs"

LICENSE = "(FTL | GPL-2.0-or-later) & MIT"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=eeb073d5fb86d23c52bb9b84aa256307 \
                    file://docs/FTL.TXT;md5=72d844cd2f3bcaf6a85244b508032be7 \
                    file://docs/GPLv2.TXT;md5=8ef380476f642c20ebf40fecb0add2ec \
                    "

SRC_URI = "${SAVANNAH_NONGNU_MIRROR}/${BPN}/${BP}.tar.xz"
SRC_URI[sha256sum] = "32427e8c471ac095853212a37aef816c60b42052d4d9e48230bab3bdf2936ccc"

UPSTREAM_CHECK_REGEX = "freetype-(?P<pver>\d+(\.\d+)+)"

inherit autotools pkgconfig multilib_header

# Adapt autotools to work with the minimal autoconf usage in freetype
AUTOTOOLS_SCRIPT_PATH = "${S}/builds/unix"
CONFIGURE_SCRIPT = "${S}/configure"
EXTRA_AUTORECONF += "--exclude=autoheader --exclude=automake -I ."

PACKAGECONFIG ??= "zlib pixmap"

PACKAGECONFIG[brotli] = "--with-brotli,--without-brotli,brotli"
PACKAGECONFIG[bzip2] = "--with-bzip2,--without-bzip2,bzip2"
# harfbuzz results in a circular dependency so enabling is non-trivial
PACKAGECONFIG[harfbuzz] = "--with-harfbuzz,--without-harfbuzz,harfbuzz"
PACKAGECONFIG[pixmap] = "--with-png,--without-png,libpng"
PACKAGECONFIG[zlib] = "--with-zlib,--without-zlib,zlib"
PACKAGECONFIG[freetypeconfig] = "--enable-freetype-config=yes,--enable-freetype-config=no,"

EXTRA_OECONF = "CC_BUILD='${BUILD_CC}'"

TARGET_CPPFLAGS += "-D_FILE_OFFSET_BITS=64"

do_install:append() {
	oe_multilib_header freetype2/freetype/config/ftconfig.h
}

BBCLASSEXTEND = "native nativesdk"
