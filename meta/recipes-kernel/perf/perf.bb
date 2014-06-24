SUMMARY = "Performance analysis tools for Linux"
DESCRIPTION = "Performance counters for Linux are a new kernel-based \
subsystem that provide a framework for all things \
performance analysis. It covers hardware level \
(CPU/PMU, Performance Monitoring Unit) features \
and software features (software counters, tracepoints) \
as well."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

PR = "r9"

require perf-features.inc

BUILDPERF_libc-uclibc = "no"

# gui support was added with kernel 3.6.35
# since 3.10 libnewt was replaced by slang
# to cover a wide range of kernel we add both dependencies
TUI_DEPENDS = "${@perf_feature_enabled('perf-tui', 'libnewt slang', '',d)}"
SCRIPTING_DEPENDS = "${@perf_feature_enabled('perf-scripting', 'perl python', '',d)}"

DEPENDS = "virtual/kernel \
           virtual/${MLPREFIX}libc \
           ${MLPREFIX}elfutils \
           ${MLPREFIX}binutils \
           ${TUI_DEPENDS} \
           ${SCRIPTING_DEPENDS} \
           bison flex \
          "

PROVIDES = "virtual/perf"

inherit linux-kernel-base kernel-arch pythonnative

# needed for building the tools/perf Python bindings
inherit python-dir
export STAGING_INCDIR
export STAGING_LIBDIR
export BUILD_SYS
export HOST_SYS

do_populate_lic[depends] += "virtual/kernel:do_populate_sysroot"

# needed for building the tools/perf Perl binding
inherit perlnative cpan-base
# Env var which tells perl if it should use host (no) or target (yes) settings
export PERLCONFIGTARGET = "${@is_target(d)}"
export PERL_INC = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}/CORE"
export PERL_LIB = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}"
export PERL_ARCHLIB = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}"

S = "${STAGING_KERNEL_DIR}"
# The source should be ready after the do_unpack
do_unpack[depends] += "virtual/kernel:do_populate_sysroot"

B = "${WORKDIR}/${BPN}-${PV}"

SCRIPTING_DEFINES = "${@perf_feature_enabled('perf-scripting', '', 'NO_LIBPERL=1 NO_LIBPYTHON=1',d)}"
TUI_DEFINES = "${@perf_feature_enabled('perf-tui', '', 'NO_NEWT=1',d)}"

# The LDFLAGS is required or some old kernels fails due missing
# symbols and this is preferred than requiring patches to every old
# supported kernel.
LDFLAGS="-ldl -lutil"

EXTRA_OEMAKE = \
		'-C ${S}/tools/perf \
		O=${B} \
		CROSS_COMPILE=${TARGET_PREFIX} \
		ARCH=${ARCH} \
		CC="${CC}" \
		AR="${AR}" \
		perfexecdir=${libexecdir} \
		NO_GTK2=1 ${TUI_DEFINES} NO_DWARF=1 ${SCRIPTING_DEFINES} \
		'

EXTRA_OEMAKE += "\
	'prefix=${prefix}' \
	'bindir=${bindir}' \
	'sharedir=${datadir}' \
	'sysconfdir=${sysconfdir}' \
	'perfexecdir=${libexecdir}/perf-core' \
	\
	'ETC_PERFCONFIG=${@oe.path.relative(prefix, sysconfdir)}' \
	'sharedir=${@oe.path.relative(prefix, datadir)}' \
	'mandir=${@oe.path.relative(prefix, mandir)}' \
	'infodir=${@oe.path.relative(prefix, infodir)}' \
"

# PPC64 uses long long for u64 in the kernel, but powerpc's asm/types.h
# prevents 64-bit userland from seeing this definition, instead defaulting
# to u64 == long in userspace. Define __SANE_USERSPACE_TYPES__ to get 
# int-ll64.h included. And MIPS64 has the same issue.
EXTRA_OEMAKE_append_powerpc64 = ' CFLAGS=-D__SANE_USERSPACE_TYPES__'
EXTRA_OEMAKE_append_mips64 = ' CFLAGS=-D__SANE_USERSPACE_TYPES__'

PARALLEL_MAKE = ""

do_compile() {
	# Linux kernel build system is expected to do the right thing
	unset CFLAGS
	oe_runmake all
}

do_install() {
	# Linux kernel build system is expected to do the right thing
	unset CFLAGS
	oe_runmake DESTDIR=${D} install
	# we are checking for this make target to be compatible with older perf versions
	if [ "${@perf_feature_enabled('perf-scripting', 1, 0, d)}" = "1" -a $(grep install-python_ext ${S}/tools/perf/Makefile) = "0" ]; then
		oe_runmake DESTDIR=${D} install-python_ext
	fi
}

do_configure_prepend () {
    sed -i 's,-Werror ,,' ${S}/tools/perf/Makefile
}

python do_package_prepend() {
    bb.data.setVar('PKGV', get_kernelversion('${S}').split("-")[0], d)
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES =+ "${PN}-archive ${PN}-tests ${PN}-perl ${PN}-python"

RDEPENDS_${PN} += "elfutils"
RDEPENDS_${PN}-archive =+ "bash"
RDEPENDS_${PN}-python =+ "bash python"
RDEPENDS_${PN}-perl =+ "bash perl perl-modules"

RSUGGESTS_SCRIPTING = "${@perf_feature_enabled('perf-scripting', '${PN}-perl ${PN}-python', '',d)}"
RSUGGESTS_${PN} += "${PN}-archive ${PN}-tests ${RSUGGESTS_SCRIPTING}"

FILES_${PN}-dbg += "${libdir}/python*/site-packages/.debug"
FILES_${PN}-archive = "${libdir}/perf/perf-core/perf-archive"
FILES_${PN}-tests = "${libdir}/perf/perf-core/tests"
FILES_${PN}-python = "${libdir}/python*/site-packages ${libdir}/perf/perf-core/scripts/python"
FILES_${PN}-perl = "${libdir}/perf/perf-core/scripts/perl"
