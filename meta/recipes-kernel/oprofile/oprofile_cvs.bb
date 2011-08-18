PV = "0.9.4+cvs${SRCDATE}"
PR = "r1"
SECTION = "devel"
SUMMARY = "System-Wide Profiler"
DESCRIPTION = "OProfile is a system-wide profiler for Linux systems, capable \
of profiling all running code at low overhead."
LICENSE = "GPL"
DEPENDS = "popt binutils"
RDEPENDS_${PN} = "binutils-symlinks"
RRECOMMENDS_${PN} = "kernel-vmlinux"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "cvs://anonymous@oprofile.cvs.sourceforge.net/cvsroot/oprofile;module=oprofile \
           file://opstart.patch \
	   file://acinclude.m4"
S = "${WORKDIR}/oprofile"

inherit autotools

EXTRA_OECONF = "--with-kernel-support \
		--without-x \
		--disable-werror "

do_configure () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
	autotools_do_configure
}
