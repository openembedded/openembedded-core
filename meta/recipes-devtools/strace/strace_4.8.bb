SUMMARY = "System call tracing tool"
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=124500c21e856f0912df29295ba104c7"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://git-version-gen \
           file://strace-add-configure-options.patch \
           file://Makefile-ptest.patch \
           file://strace-fix-64-bit-process-detection.patch \
           file://run-ptest \
           file://0001-Work-around-conflict-between-sys-ptrace.h-and-linux-.patch \
          "

SRC_URI[md5sum] = "c575ef43829586801f514fd91bfe7575"
SRC_URI[sha256sum] = "f492291f07a7c805c07a8395cce1ea054a6401ad414f4cc12185672215e1d7f8"

inherit autotools ptest
RDEPENDS_${PN}-ptest += "make"

PACKAGECONFIG_class-target ?= "libaio ${@bb.utils.contains('DISTRO_FEATURES', 'acl', 'acl', '', d)}"

PACKAGECONFIG[libaio] = "--enable-aio,--disable-aio,libaio"
PACKAGECONFIG[acl] = "--enable-acl,--disable-acl,acl"

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
