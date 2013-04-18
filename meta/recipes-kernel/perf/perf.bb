SUMMARY = "Performance analysis tools for Linux"
DESCRIPTION = "Performance counters for Linux are a new kernel-based \
subsystem that provide a framework for all things \
performance analysis. It covers hardware level \
(CPU/PMU, Performance Monitoring Unit) features \
and software features (software counters, tracepoints) \
as well."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

PR = "r8"

require perf-features.inc

BUILDPERF_libc-uclibc = "no"

TUI_DEPENDS = "${@perf_feature_enabled('perf-tui', 'libnewt', '',d)}"
SCRIPTING_DEPENDS = "${@perf_feature_enabled('perf-scripting', 'perl python', '',d)}"

DEPENDS = "virtual/kernel \
           virtual/${MLPREFIX}libc \
           ${MLPREFIX}elfutils \
           ${MLPREFIX}binutils \
           ${TUI_DEPENDS} \
           ${SCRIPTING_DEPENDS} \
           bison flex \
          "

SCRIPTING_RDEPENDS = "${@perf_feature_enabled('perf-scripting', 'perl perl-modules python', '',d)}"
RDEPENDS_${PN} += "elfutils bash ${SCRIPTING_RDEPENDS}"

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
B = "${WORKDIR}/${BPN}-${PV}"

SCRIPTING_DEFINES = "${@perf_feature_enabled('perf-scripting', '', 'NO_LIBPERL=1 NO_LIBPYTHON=1',d)}"
TUI_DEFINES = "${@perf_feature_enabled('perf-tui', '', 'NO_NEWT=1',d)}"

export LDFLAGS = "-ldl -lutil"
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

# We already pass the correct arguments to our compiler for the CFLAGS (if we
# don't override it, it'll add -m32/-m64 itself). For LDFLAGS, it was failing
# to find bfd symbols.
EXTRA_OEMAKE += "\
	'CFLAGS=${CFLAGS}' \
	'LDFLAGS=${LDFLAGS} -lpthread -lrt -lelf -lm -lbfd' \
	\
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

PARALLEL_MAKE = ""

do_compile() {
	oe_runmake all
}

do_install() {
	oe_runmake DESTDIR=${D} install
	# we are checking for this make target to be compatible with older perf versions
	if [ "${@perf_feature_enabled('perf-scripting', 1, 0, d)}" = "1" -a $(grep install-python_ext ${S}/tools/perf/Makefile) = "0"]; then
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

FILES_${PN} += "${libexecdir}/perf-core"
FILES_${PN}-dbg += "${libdir}/python*/site-packages/.debug"
FILES_${PN} += "${libdir}/python*/site-packages"
