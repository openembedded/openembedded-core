SUMMARY = "Freetype font rendering library"
DESCRIPTION = "FreeType is a software font engine that is designed to be small, efficient, \
highly customizable, and portable while capable of producing high-quality output (glyph \
images). It can be used in graphics libraries, display servers, font conversion tools, text \
image generation tools, and many other products as well."
HOMEPAGE = "http://www.freetype.org/"
BUGTRACKER = "https://savannah.nongnu.org/bugs/?group=freetype"

LICENSE = "FreeType | GPLv2+"
LIC_FILES_CHKSUM = "file://docs/LICENSE.TXT;md5=28d5381b1bef2649c59f20c20bae4f39 \
                    file://docs/FTL.TXT;md5=d479e83797f699fe873b38dadd0fcd4c \
                    file://docs/GPLv2.TXT;md5=8ef380476f642c20ebf40fecb0add2ec"

SECTION = "libs"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2 \
           file://no-hardcode.patch"

SRC_URI[md5sum] = "dbf2caca1d3afd410a29217a9809d397"
SRC_URI[sha256sum] = "a9eb7da3875fcb2f022a9c280c01b94ae45ac83d8102838c05dce1277948fb71"

S = "${WORKDIR}/freetype-${PV}"

inherit autotools pkgconfig binconfig

LIBTOOL = "${S}/builds/unix/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
EXTRA_OEMAKE_virtclass-native = ""
EXTRA_OECONF = "--without-zlib --without-bzip2"

do_configure() {
	cd builds/unix
	libtoolize --force --copy
	aclocal -I .
	gnu-configize --force
	autoconf
	cd ${S}
	oe_runconf
}

do_configure_virtclass-native() {
	(cd builds/unix && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_compile_prepend() {
	${BUILD_CC} -o objs/apinames src/tools/apinames.c
}

FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-dev += "${bindir}"

BBCLASSEXTEND = "native"

