SUMMARY = "XML::Parser - A perl module for parsing XML documents"
DESCRIPTION = "This module provides ways to parse XML documents. \
               It is built on top of XML::Parser::Expat, which is \
               a lower level interface to James Clark's expat \
               library."
HOMEPAGE = "https://metacpan.org/release/XML-Parser"
BUGTRACKER = "https://github.com/cpan-authors/XML-Parser/issues"
SECTION = "libs"
LICENSE = "Artistic-1.0 OR GPL-1.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=71d8bc02b6854eabd7589eb667758414"

CVE_PRODUCT = "toddr:xml::parser"

DEPENDS += "expat libfile-sharedir-install-perl-native"

SRC_URI = "${CPAN_MIRROR}/authors/id/T/TO/TODDR/XML-Parser-${PV}.tar.gz \
           file://0001-Makefile.PL-make-check_lib-cross-friendly.patch \
           "
SRC_URI[sha256sum] = "a358fd7c49f5e27717a644a9102bd21dc7fc25a415983279c59b1580e2b62a58"

S = "${UNPACKDIR}/XML-Parser-${PV}"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR} CC='${CC}' LD='${CCLD}' FULL_AR='${AR}'"

inherit cpan pkgconfig ptest-perl

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}

do_compile:class-native() {
	cpan_do_compile
}

do_install_ptest() {
	sed -i -e "s:/usr/local/bin/perl:/usr/bin/perl:g" ${B}/samples/xmlstats
	sed -i -e "s:/usr/local/bin/perl:/usr/bin/perl:g" ${B}/samples/xmlfilter
	sed -i -e "s:/usr/local/bin/perl:/usr/bin/perl:g" ${B}/samples/xmlcomments
	sed -i -e "s:/usr/local/bin/perl:/usr/bin/perl:g" ${B}/samples/canonical
	cp -r ${B}/samples ${D}${PTEST_PATH}
	chown -R root:root ${D}${PTEST_PATH}/samples
	# t/checklib_*.t inspect the bundled Devel::CheckLib under ./inc
	cp -r ${B}/inc ${D}${PTEST_PATH}
	chown -R root:root ${D}${PTEST_PATH}/inc
}

RDEPENDS:${PN} += "\
    libfile-sharedir-perl \
    perl-module-carp \
    perl-module-file-spec \
    perl-module-scalar-util \
    perl-module-warnings \
"
RDEPENDS:${PN}-ptest += "\
    perl-module-config \
    perl-module-file-temp \
    perl-module-filehandle \
    perl-module-if \
    perl-module-lib \
    perl-module-test-more \
    perl-module-text-parsewords \
    perl-module-vars \
"

BBCLASSEXTEND = "native nativesdk"
