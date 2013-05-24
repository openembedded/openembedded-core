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

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "3463102764315eb86c0d3c2e1f3ffb7d"
SRC_URI[sha256sum] = "a78a17486689ab6852a9e1a759b179827ac9dfd7e2f237ddf169c73398c85381"

S = "${WORKDIR}/freetype-${PV}"

inherit autotools pkgconfig binconfig

LIBTOOL = "${S}/builds/unix/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
EXTRA_OEMAKE_class-native = ""
EXTRA_OECONF = "--without-zlib --without-bzip2 CC_BUILD='${BUILD_CC}'"

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

BBCLASSEXTEND = "native"

