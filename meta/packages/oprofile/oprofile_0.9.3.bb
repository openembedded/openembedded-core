SECTION = "devel"
DESCRIPTION = "OProfile is a system-wide profiler for Linux systems, capable \
of profiling all running code at low overhead."
LICENSE = "GPL"
DEPENDS = "popt binutils"
RDEPENDS = "binutils-symlinks"
RRECOMMENDS = "kernel-vmlinux"
PR = "r5"

SRC_URI = "${SOURCEFORGE_MIRROR}/oprofile/oprofile-${PV}.tar.gz \
           file://armv6_fix.patch;patch=1 \
           file://oparchive_fix.patch;patch=1 \
           file://root_option.patch;patch=1 \
           file://opstart.patch;patch=1 \
           file://fix-arith.patch;patch=1;pnum=0 \
           file://acinclude.m4"
S = "${WORKDIR}/oprofile-${PV}"

inherit autotools

# NOTE: this disables the build of the kernel modules.
# Should add the oprofile kernel modules, for those with 2.4
# kernels, as a seperate .oe file.
EXTRA_OECONF = "--with-kernel-support \
		--without-x"

do_configure () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
	autotools_do_configure
}
