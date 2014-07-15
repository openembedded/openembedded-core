SUMMARY = "System-Wide Profiler"
DESCRIPTION = "OProfile is a system-wide profiler for Linux systems, capable \
of profiling all running code at low overhead."
HOMEPAGE = "http://oprofile.sourceforge.net/news/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=16191&atid=116191"

LICENSE = "LGPLv2.1+ & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://libopagent/opagent.h;beginline=5;endline=26;md5=4f16f72c7a493d8a4704aa18d03d15c6 \
                   "
SECTION = "devel"

DEPENDS = "popt binutils"
RDEPENDS_${PN} = "binutils-symlinks"
RRECOMMENDS_${PN} = "kernel-vmlinux"

FILES_${PN} = "${bindir} ${libdir}/${BPN}/lib*${SOLIBS} ${datadir}/${BPN}"
FILES_${PN}-dev += "${libdir}/${BPN}/lib*${SOLIBSDEV} ${libdir}/${BPN}/lib*.la"
FILES_${PN}-staticdev += "${libdir}/${BPN}/lib*.a"

SRC_URI = "file://opstart.patch \
           file://acinclude.m4 \
           file://automake-foreign.patch \
           file://oprofile-cross-compile-tests.patch \
           file://run-ptest \
           file://root-home-dir.patch"

inherit autotools-brokensep pkgconfig ptest

EXTRA_OECONF = "--with-kernel=${STAGING_KERNEL_DIR}  --without-x"
do_configure () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
	autotools_do_configure
}

EXTRA_OEMAKE = "SRCDIR=${PTEST_PATH}/libutil++/tests"
do_compile_ptest() {
	oe_runmake check
}

do_install_ptest() {
	subdirs="libdb/tests libutil++/tests libregex/tests libutil/tests libop/tests libdb/tests "
	cd ${S}
	for tooltest in ${subdirs}
	do
		find ${tooltest} -perm /u=x -type f| cpio -pvdu ${D}${PTEST_PATH}
	done

	# needed by some libop tests
	cp -r events ${D}${PTEST_PATH}

	# needed by libregex regex_test
	cp libregex/stl.pat ${D}${PTEST_PATH}/libregex
	cp libregex/tests/mangled-name ${D}${PTEST_PATH}/libregex/tests

	# needed by litutil++ file_manip_tests
	cp libutil++/tests/file_manip_tests.cpp \
		libutil++/tests/file_manip_tests.o ${D}${PTEST_PATH}/libutil++/tests
}
