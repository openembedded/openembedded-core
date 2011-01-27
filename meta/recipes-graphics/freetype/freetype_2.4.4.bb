SUMMARY = "Freetype font rendering library"
DESCRIPTION = "FreeType is a software font engine that is designed to be small, efficient, \
highly customizable, and portable while capable of producing high-quality output (glyph \
images). It can be used in graphics libraries, display servers, font conversion tools, text \
image generation tools, and many other products as well."
HOMEPAGE = "http://www.freetype.org/"
BUGTRACKER = "https://savannah.nongnu.org/bugs/?group=freetype"

LICENSE = "FreeTypeLicense | GPLv2+"
LIC_FILES_CHKSUM = "file://docs/LICENSE.TXT;md5=8bc1a580aeb518100d00a2dd29e68edf \
                    file://docs/FTL.TXT;md5=d479e83797f699fe873b38dadd0fcd4c \
                    file://docs/GPL.TXT;md5=8ef380476f642c20ebf40fecb0add2ec"

SECTION = "libs"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2 \
           file://no-hardcode.patch"

SRC_URI[md5sum] = "b3e2b6e2f1c3e0dffa1fd2a0f848b671"
SRC_URI[sha256sum] = "4b8281c7dc4d375c6b65d3c6f4808e488a313fab47d7be82aad2c871c8480fef"

S = "${WORKDIR}/freetype-${PV}"

inherit autotools pkgconfig binconfig

LIBTOOL = "${S}/builds/unix/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
EXTRA_OEMAKE_virtclass-native = ""
EXTRA_OECONF = "--without-zlib"

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

