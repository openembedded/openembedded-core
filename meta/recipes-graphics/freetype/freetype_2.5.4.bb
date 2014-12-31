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

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2"
SRC_URI[md5sum] = "438585445a2f1f71c77590155fd96616"
SRC_URI[sha256sum] = "3b4e2c666b33b32ad29ff0fe5c6538e757d3c73368e5dfa88bd59b4bc95cbabb"

BINCONFIG = "${bindir}/freetype-config"

inherit autotools-brokensep pkgconfig binconfig-disabled multilib_header

LIBTOOL = "${S}/builds/unix/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
EXTRA_OEMAKE_class-native = ""
EXTRA_OECONF = "--without-zlib --without-bzip2 CC_BUILD='${BUILD_CC}'"
TARGET_CPPFLAGS += "-D_FILE_OFFSET_BITS=64"


PACKAGECONFIG ??= ""
PACKAGECONFIG[pixmap] = "--with-png,--without-png,libpng"
# This results in a circular dependency so enabling is non-trivial
PACKAGECONFIG[harfbuzz] = "--with-harfbuzz,--without-harfbuzz,harfbuzz"

do_configure() {
	cd builds/unix
	libtoolize --force --copy
	aclocal -I .
	gnu-configize --force
	autoconf
	cd ${S}
	oe_runconf
}

do_configure_class-native() {
	(cd builds/unix && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_compile_prepend() {
	${BUILD_CC} -o objs/apinames src/tools/apinames.c
}

do_install_append() {
	oe_multilib_header freetype2/config/ftconfig.h
}

BBCLASSEXTEND = "native"

