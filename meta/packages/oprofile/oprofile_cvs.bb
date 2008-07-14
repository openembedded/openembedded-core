PV = "0.9.3+cvs${SRCDATE}"
PR = "r4"
SECTION = "devel"
DESCRIPTION = "OProfile is a system-wide profiler for Linux systems, capable \
of profiling all running code at low overhead."
LICENSE = "GPL"
DEPENDS = "popt binutils"
RDEPENDS = "binutils-symlinks"
RRECOMMENDS = "kernel-vmlinux"

SRC_URI = "cvs://anonymous@oprofile.cvs.sourceforge.net/cvsroot/oprofile;module=oprofile \
           file://opstart.patch;patch=1 \
           file://fix_debug_search.patch;patch=1;pnum=0 \
	   file://op-cross-compile.patch;patch=1;pnum=0 \
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
