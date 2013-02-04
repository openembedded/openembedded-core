pickDESCRIPTION = "Python hardware latency detector"
HOMEPAGE = "http://git.kernel.org/?p=linux/kernel/git/clrkwllms/rt-tests.git"
SECTION = "tests"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
RDEPENDS_${PN} = "python python-subprocess python-textutils"
RRECOMMENDS_${PN} = "kernel-module-hwlat-detector"

require rt-tests.inc

PR = "${INC_PR}.0"

EXTRA_OEMAKE += "PYLIB=${libdir}/python${PYTHON_BASEVERSION}/dist-packages"

do_compile() {
	oe_runmake hwlatdetect
}

do_install() {
        oe_runmake install_hwlatdetect DESTDIR=${D} SBINDIR=${sbindir} \
	           MANDIR=${mandir} INCLUDEDIR=${includedir}
}

FILES_${PN} += "${libdir}/python${PYTHON_BASEVERSION}/dist-packages/hwlatdetect.py"
