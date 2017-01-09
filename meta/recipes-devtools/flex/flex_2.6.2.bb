SUMMARY = "Flex (The Fast Lexical Analyzer)"
DESCRIPTION = "Flex is a fast lexical analyser generator.  Flex is a tool for generating programs that recognize \
lexical patterns in text."
HOMEPAGE = "http://sourceforge.net/projects/flex/"
SECTION = "devel"
LICENSE = "BSD"

DEPENDS = "${@bb.utils.contains('PTEST_ENABLED', '1', 'bison-native flex-native', '', d)}"

BBCLASSEXTEND = "native nativesdk"

LIC_FILES_CHKSUM = "file://COPYING;md5=e4742cf92e89040b39486a6219b68067"

SRC_URI = "https://github.com/westes/flex/releases/download/v${PV}/flex-${PV}.tar.gz \
           file://crosscompile.patch;apply=1 \
           file://run-ptest \
           file://0001-tests-add-a-target-for-building-tests-without-runnin.patch \
           ${@bb.utils.contains('PTEST_ENABLED', '1', '', 'file://disable-tests.patch', d)} \
           "

SRC_URI[md5sum] = "cc6d76c333db7653d5caf423a3335239"
SRC_URI[sha256sum] = "9a01437a1155c799b7dc2508620564ef806ba66250c36bf5f9034b1c207cb2c9"

# Flex has moved to github from 2.6.1 onwards
UPSTREAM_CHECK_URI = "https://github.com/westes/flex/releases"
UPSTREAM_CHECK_REGEX = "flex-(?P<pver>\d+(\.\d+)+)\.tar"

inherit autotools gettext texinfo ptest

M4 = "${bindir}/m4"
M4_class-native = "${STAGING_BINDIR_NATIVE}/m4"
EXTRA_OECONF += "ac_cv_path_M4=${M4}"
EXTRA_OEMAKE += "m4=${STAGING_BINDIR_NATIVE}/m4"

EXTRA_OEMAKE += "${@bb.utils.contains('PTEST_ENABLED', '1', 'FLEX=${STAGING_BINDIR_NATIVE}/flex', '', d)}"

do_install_append_class-native() {
	create_wrapper ${D}/${bindir}/flex M4=${M4}
}

do_install_append_class-nativesdk() {
	create_wrapper ${D}/${bindir}/flex M4=${M4}
}

RDEPENDS_${PN} += "m4"
RDEPENDS_${PN}-ptest += "bash gawk"

do_compile_ptest() {
	oe_runmake -C ${B}/tests -f ${B}/tests/Makefile top_builddir=${B} INCLUDES=-I${S}/src buildtests
}

do_install_ptest() {
	mkdir -p ${D}${PTEST_PATH}/build-aux/
	cp ${S}/build-aux/test-driver ${D}${PTEST_PATH}/build-aux/
	cp -r ${S}/tests/* ${D}${PTEST_PATH}
	cp -r ${B}/tests/* ${D}${PTEST_PATH}
	sed -e 's/^Makefile:/_Makefile:/' \
	    -e 's/^srcdir = \(.*\)/srcdir = ./' -e 's/^top_srcdir = \(.*\)/top_srcdir = ./' \
	    -e 's/^builddir = \(.*\)/builddir = ./' -e 's/^top_builddir = \(.*\)/top_builddir = ./' \
	    -i ${D}${PTEST_PATH}/Makefile
}
