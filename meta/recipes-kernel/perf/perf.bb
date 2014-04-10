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

#kernel 3.1+ supports WERROR to disable warnings as errors
export WERROR = "0"

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
	'ETC_PERFCONFIG=${@os.path.relpath(sysconfdir, prefix)}' \
	'sharedir=${@os.path.relpath(datadir, prefix)}' \
	'mandir=${@os.path.relpath(mandir, prefix)}' \
	'infodir=${@os.path.relpath(infodir, prefix)}' \
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
	if [ "${@perf_feature_enabled('perf-scripting', 1, 0, d)}" = "1" -a $(grep install-python_ext ${S}/tools/perf/Makefile) = "0"]; then
		oe_runmake DESTDIR=${D} install-python_ext
	fi
}

do_configure_prepend () {
    #kernels before 3.1 do not support WERROR env variable
    sed -i 's,-Werror ,,' ${S}/tools/perf/Makefile
    if [ -e "${S}/tools/perf/config/Makefile" ]; then
        sed -i 's,-Werror ,,' ${S}/tools/perf/config/Makefile
    fi

    # If building a multlib based perf, the incorrect library path will be
    # detected by perf, since it triggers via: ifeq ($(ARCH),x86_64). In a 32 bit
    # build, with a 64 bit multilib, the arch won't match and the detection of a 
    # 64 bit build (and library) are not exected. To ensure that libraries are
    # installed to the correct location, we can make the substitution in the 
    # config/Makefile. For non multilib builds, this has no impact.
    if [ -e "${S}/tools/perf/config/Makefile" ]; then
        sed -i 's,libdir = $(prefix)/$(lib),libdir = $(prefix)/${baselib},' ${S}/tools/perf/config/Makefile
    fi
    # We need to ensure the --sysroot option in CC is preserved
    if [ -e "${S}/tools/perf/Makefile.perf" ]; then
        sed -i 's,CC = $(CROSS_COMPILE)gcc,#CC,' ${S}/tools/perf/Makefile.perf
        sed -i 's,AR = $(CROSS_COMPILE)ar,#AR,' ${S}/tools/perf/Makefile.perf
    fi
    if [ -e "${S}/tools/lib/api/Makefile" ]; then
        sed -i 's,CC = $(CROSS_COMPILE)gcc,#CC,' ${S}/tools/lib/api/Makefile
        sed -i 's,AR = $(CROSS_COMPILE)ar,#AR,' ${S}/tools/lib/api/Makefile
    fi
    if [ -e "${S}/tools/perf/config/feature-checks/Makefile" ]; then
        sed -i 's,CC := $(CROSS_COMPILE)gcc -MD,CC += -MD,' ${S}/tools/perf/config/feature-checks/Makefile
    fi
}

python do_package_prepend() {
    bb.data.setVar('PKGV', get_kernelversion('${S}').split("-")[0], d)
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} += "${libexecdir}/perf-core ${exec_prefix}/libexec/perf-core ${libdir}/traceevent"
FILES_${PN}-dbg += "${libdir}/python*/site-packages/.debug"
FILES_${PN} += "${libdir}/python*/site-packages"

INHIBIT_PACKAGE_DEBUG_SPLIT="1"
