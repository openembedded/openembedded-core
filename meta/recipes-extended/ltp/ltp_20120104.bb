SUMMARY = "Linux Test Project"
DESCRIPTION = "The Linux Test Project is a joint project with SGI, IBM, OSDL, and Bull with a goal to deliver test suites to the open source community that validate the reliability, robustness, and stability of Linux. The Linux Test Project is a collection of tools for testing the Linux kernel and related features."
HOMEPAGE = "http://ltp.sourceforge.net"
SECTION = "console/utils"

LICENSE = "GPLv2 & GPLv2+ & LGPLv2+ & LGPLv2.1+ & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
		    file://testcases/kernel/mce-test/COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
		    file://testcases/kernel/controllers/freezer/COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
		    file://testcases/kernel/controllers/freezer/run_freezer.sh;startline=5;endline=17;md5=aeac3f7691caa2e76fd5a732fbd6510d \
		    file://testcases/kernel/fs/ext4-new-features/ffsb-6.0-rc2/COPYING;md5=c46082167a314d785d012a244748d803 \
		    file://testcases/kernel/hotplug/memory_hotplug/COPYING;md5=e04a2e542b2b8629bf9cd2ba29b0fe41 \
		    file://testcases/kernel/hotplug/cpu_hotplug/COPYING;md5=e04a2e542b2b8629bf9cd2ba29b0fe41 \
		    file://testcases/open_posix_testsuite/COPYING;md5=216e43b72efbe4ed9017cc19c4c68b01 \
		    file://tools/netpipe-2.4/COPYING;md5=18810669f13b87348459e611d31ab760 \
		    file://tools/netpipe-2.4-ipv6/COPYING;md5=18810669f13b87348459e611d31ab760 \
		    file://tools/top-LTP/proc/COPYING;md5=6e29c688d912da12b66b73e32b03d812 \
		    file://tools/pounder21/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://utils/benchmark/kernbench-0.42/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
		"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/ltp/ltp-full-${PV}.bz2 \
           file://uclibc-getcontext.patch \
          "

SRC_URI[md5sum] = "ac183bbdac67139f049b3755fda37179"
SRC_URI[sha256sum] = "11b149b821d709e3351ac66f028f1b0dd5337ee9073dc20019ccffdac42d5c2e"

export prefix = "/opt/ltp"
export exec_prefix = "/opt/ltp"

inherit autotools

FILES_${PN}-dbg += "/opt/ltp/runtest/.debug"
FILES_${PN}-dbg += "/opt/ltp/testcases/bin/.debug"
FILES_${PN}-dbg += "/opt/ltp/testcases/bin/*/bin/.debug"
FILES_${PN}-dbg += "/opt/ltp/testcases/bin/*/test/.debug"
FILES_${PN}-dbg += "/opt/ltp/scenario_groups/.debug"
FILES_${PN}-dbg += "/opt/ltp/testscripts/.debug"
FILES_${PN}-dbg += "/opt/ltp/testscripts/open_posix_testsuite/.debug"

FILES_${PN}-staticdev += "/opt/ltp/lib/libmem.a"

FILES_${PN} += "/opt/ltp/* /opt/ltp/runtest/* /opt/ltp/scenario_groups/* /opt/ltp/testcases/bin/* /opt/ltp/testcases/bin/*/bin/* /opt/ltp/testscripts/* /opt/ltp/testcases/open_posix_testsuite/* /opt/ltp/testcases/open_posix_testsuite/conformance/* /opt/ltp/testcases/open_posix_testsuite/Documentation/* /opt/ltp/testcases/open_posix_testsuite/functional/* /opt/ltp/testcases/open_posix_testsuite/include/* /opt/ltp/testcases/open_posix_testsuite/scripts/* /opt/ltp/testcases/open_posix_testsuite/stress/* /opt/ltp/testcases/open_posix_testsuite/tools/*"

TARGET_CC_ARCH += "${LDFLAGS}"

do_unpack_append() {
        bb.build.exec_func('do_extract_tarball', d)
}

do_extract_tarball() {
	if test -f ${WORKDIR}/ltp-full-${PV} ; then
	    tar x --no-same-owner -f ${WORKDIR}/ltp-full-${PV} -C ${WORKDIR}
	    rm -rf ${WORKDIR}/ltp-${PV}
	    mv ${WORKDIR}/ltp-full-${PV} ${WORKDIR}/ltp-${PV}
	fi
}

do_install(){
	install -d ${D}/opt/ltp/
	oe_runmake DESTDIR=${D} SKIP_IDCHECK=1 install
	
	# Copy POSIX test suite into ${D}/opt/ltp/testcases by manual
	cp -r testcases/open_posix_testsuite ${D}/opt/ltp/testcases

	# We need to remove all scripts which depend on /usr/bin/expect, since expect is not supported in poky
	# We will add expect for enhancement in future
	find ${D} -type f -print | xargs grep "\!.*\/usr\/bin\/expect" | awk -F":" '{print $1}' | xargs rm -f
}

# Avoid generated binaries stripping. Otherwise some of the ltp tests such as ldd01 & nm01 fails
INHIBIT_PACKAGE_STRIP = "1"

