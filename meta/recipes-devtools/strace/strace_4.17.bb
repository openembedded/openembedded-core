SUMMARY = "System call tracing tool"
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=488acb3aaaf5d14a2e1a852d13668a70"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://disable-git-version-gen.patch \
           file://more-robust-test-for-m32-mx32-compile-support.patch \
           file://update-gawk-paths.patch \
           file://Makefile-ptest.patch \
           file://run-ptest \
           file://0001-Fix-build-when-using-non-glibc-libc-implementation-o.patch \
           file://mips-SIGEMT.patch \
           file://0001-caps-abbrev.awk-fix-gawk-s-path.patch \
           file://0008-replace-struct-ucontext-with-ucontext_t.patch \
           "

SRC_URI[md5sum] = "8d7eb10eba68bad83a269197e634b626"
SRC_URI[sha256sum] = "81f35b085fbb3cfa806eb521a8522ac3406deaccfe121ce35064bad268237419"

inherit autotools ptest bluetooth

RDEPENDS_${PN}-ptest += "make coreutils grep gawk sed"

PACKAGECONFIG_class-target ??= "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
"

PACKAGECONFIG[bluez] = "ac_cv_header_bluetooth_bluetooth_h=yes,ac_cv_header_bluetooth_bluetooth_h=no,${BLUEZ}"
PACKAGECONFIG[libunwind] = "--with-libunwind,--without-libunwind,libunwind"

TESTDIR = "tests"

do_install_append() {
	# We don't ship strace-graph here because it needs perl
	rm ${D}${bindir}/strace-graph
}

do_compile_ptest() {
	oe_runmake -C ${TESTDIR} buildtest-TESTS
}

do_install_ptest() {
	oe_runmake -C ${TESTDIR} install-ptest BUILDDIR=${B} DESTDIR=${D}${PTEST_PATH} TESTDIR=${TESTDIR}
	sed -i -e '/^src/s/strace.*[1-9]/ptest/' ${D}/${PTEST_PATH}/${TESTDIR}/Makefile
}

BBCLASSEXTEND = "native"
TOOLCHAIN = "gcc"
