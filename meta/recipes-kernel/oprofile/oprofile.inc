SUMMARY = "System-Wide Profiler"
DESCRIPTION = "OProfile is a system-wide profiler for Linux systems, capable \
of profiling all running code at low overhead."
HOMEPAGE = "http://oprofile.sourceforge.net/news/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=16191&atid=116191"

LICENSE = "LGPLv2.1+ & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://libopagent/opagent.h;beginline=5;endline=26;md5=4f16f72c7a493d8a4704aa18d03d15c6 \
                    file://daemon/liblegacy/p_module.h;beginline=2;endline=20;md5=fc23a43455edf185307274a99730b6e4"

SECTION = "devel"

DEPENDS = "popt binutils"
RDEPENDS_${PN} = "binutils-symlinks"
RRECOMMENDS_${PN} = "kernel-vmlinux"

FILES_${PN} = "${bindir} ${libdir}/${BPN}/lib*${SOLIBS} ${datadir}/${BPN}"
FILES_${PN}-dev += "${libdir}/${BPN}/lib*${SOLIBSDEV} ${libdir}/${BPN}/lib*.la"
FILES_${PN}-staticdev += "${libdir}/${BPN}/lib*.a"

INC_PR = "r2"

SRC_URI = "file://opstart.patch \
           file://oprofile-no-query-modules.patch \
           file://oprofile-root.patch \
           file://acinclude.m4"

inherit autotools-brokensep

EXTRA_OECONF = "--with-kernel=${STAGING_KERNEL_DIR}  --without-x"
do_configure () {
	find ${S} -wholename '${S}/.pc' -prune -o -type f -print | xargs sed -i 's#ROOTHOME#${ROOT_HOME}#'
	cp ${WORKDIR}/acinclude.m4 ${S}/
	autotools_do_configure
}
