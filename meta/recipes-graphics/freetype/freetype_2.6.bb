SUMMARY = "Freetype font rendering library"
DESCRIPTION = "FreeType is a software font engine that is designed to be small, efficient, \
highly customizable, and portable while capable of producing high-quality output (glyph \
images). It can be used in graphics libraries, display servers, font conversion tools, text \
image generation tools, and many other products as well."
HOMEPAGE = "http://www.freetype.org/"
BUGTRACKER = "https://savannah.nongnu.org/bugs/?group=freetype"

LICENSE = "FreeType | GPLv2+"
LIC_FILES_CHKSUM = "file://docs/LICENSE.TXT;md5=c017ff17fc6f0794adf93db5559ccd56 \
                    file://docs/FTL.TXT;md5=d479e83797f699fe873b38dadd0fcd4c \
                    file://docs/GPLv2.TXT;md5=8ef380476f642c20ebf40fecb0add2ec"

SECTION = "libs"

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2 \
           file://use-right-libtool.patch"
SRC_URI[md5sum] = "5682890cb0267f6671dd3de6eabd3e69"
SRC_URI[sha256sum] = "8469fb8124764f85029cc8247c31e132a2c5e51084ddce2a44ea32ee4ae8347e"

BINCONFIG = "${bindir}/freetype-config"

inherit autotools pkgconfig binconfig-disabled multilib_header

EXTRA_OECONF = "CC_BUILD='${BUILD_CC}'"
TARGET_CPPFLAGS += "-D_FILE_OFFSET_BITS=64"

PACKAGECONFIG ??= "zlib"
PACKAGECONFIG[bzip2] = "--with-bzip2,--without-bzip2,bzip2"
PACKAGECONFIG[zlib] = "--with-zlib,--without-zlib,zlib"
PACKAGECONFIG[pixmap] = "--with-png,--without-png,libpng"
# This results in a circular dependency so enabling is non-trivial
PACKAGECONFIG[harfbuzz] = "--with-harfbuzz,--without-harfbuzz,harfbuzz"

do_configure() {
	# Need this because although the autotools infrastructure is in
	# builds/unix the configure script get written to ${S}, so we can't
	# just use AUTOTOOLS_SCRIPT_PATH.
	cd ${S}/builds/unix
	libtoolize --force --copy
	aclocal -I .
	gnu-configize --force
	autoconf
	cd ${B}
	oe_runconf
}

do_install_append() {
	oe_multilib_header freetype2/config/ftconfig.h
}

BBCLASSEXTEND = "native"
