SUMMARY = "System call tracing tool"
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=124500c21e856f0912df29295ba104c7"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://0001-Add-linux-aarch64-arch_regs.h.patch \
           file://git-version-gen \
           file://strace-add-configure-options.patch \
           file://Makefile-ptest.patch \
           file://run-ptest \
          "

SRC_URI[md5sum] = "107a5be455493861189e9b57a3a51912"
SRC_URI[sha256sum] = "e6180d866ef9e76586b96e2ece2bfeeb3aa23f5cc88153f76e9caedd65e40ee2"

inherit autotools ptest
RDEPENDS_${PN}-ptest += "make coreutils grep gawk"

PACKAGECONFIG_class-target ?= "libaio ${@bb.utils.contains('DISTRO_FEATURES', 'acl', 'acl', '', d)}"

PACKAGECONFIG[libaio] = "--enable-aio,--disable-aio,libaio"
PACKAGECONFIG[acl] = "--enable-acl,--disable-acl,acl"
PACKAGECONFIG[libunwind] = "--with-libunwind, --without-libunwind, libunwind"

TESTDIR = "tests"

do_configure_prepend() {
	cp ${WORKDIR}/git-version-gen ${S}
}

do_install_append() {
	# We don't ship strace-graph here because it needs perl
	rm ${D}${bindir}/strace-graph
}

do_compile_ptest() {
	oe_runmake -C ${TESTDIR} buildtest-TESTS OS=linux ARCH="${TARGET_ARCH}"
}

do_install_ptest() {
	oe_runmake -C ${TESTDIR} install-ptest BUILDDIR=${B} DESTDIR=${D}${PTEST_PATH} TESTDIR=${TESTDIR}
}

BBCLASSEXTEND = "native"
