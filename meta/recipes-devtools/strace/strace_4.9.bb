SUMMARY = "System call tracing tool"
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=124500c21e856f0912df29295ba104c7"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://git-version-gen \
           file://strace-add-configure-options.patch \
           file://Makefile-ptest.patch \
           file://run-ptest \
          "

SRC_URI[md5sum] = "885eafadb10f6c60464a266d3929a2a4"
SRC_URI[sha256sum] = "095bfea5c540b91d297ccac73b21b92fd54a24599fd70395db87ff9eb7fd6f65"

inherit autotools ptest
RDEPENDS_${PN}-ptest += "make coreutils grep gawk"

PACKAGECONFIG_class-target ?= "libaio ${@bb.utils.contains('DISTRO_FEATURES', 'acl', 'acl', '', d)}"

PACKAGECONFIG[libaio] = "--enable-aio,--disable-aio,libaio"
PACKAGECONFIG[acl] = "--enable-acl,--disable-acl,acl"
PACKAGECONFIG[libunwind] = "--with-libunwind, --without-libunwind, libunwind"

export INCLUDES = "-I. -I./linux"

TESTDIR = "tests"

do_configure_prepend() {
	cp ${WORKDIR}/git-version-gen ${S}
}

do_install_append() {
	# We don't ship strace-graph here because it needs perl
	rm ${D}${bindir}/strace-graph
}

do_compile_ptest() {
	oe_runmake -C ${TESTDIR} buildtest-TESTS
}

do_install_ptest() {
	oe_runmake -C ${TESTDIR} install-ptest BUILDDIR=${B} DESTDIR=${D}${PTEST_PATH} TESTDIR=${TESTDIR}
}

BBCLASSEXTEND = "native"
