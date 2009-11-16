#
# This is for perl modules that use the old Makefile.PL build system
#
inherit cpan-base

EXTRA_CPANFLAGS ?= ""

# Env var which tells perl if it should use host (no) or target (yes) settings
export PERLCONFIGTARGET = "${@is_target(d)}"

# Env var which tells perl where the perl include files are
export PERL_INC = "${STAGING_LIBDIR}/perl/${@get_perl_version(d)}/CORE"
export PERL_LIB = "${STAGING_DATADIR}/perl/${@get_perl_version(d)}"
export PERL_ARCHLIB = "${STAGING_LIBDIR}/perl/${@get_perl_version(d)}"

cpan_do_configure () {
	yes '' | perl Makefile.PL ${EXTRA_CPANFLAGS}
	if [ "${BUILD_SYS}" != "${HOST_SYS}" ]; then
		. ${STAGING_LIBDIR}/perl/config.sh
		if [ "${IS_NEW_PERL}" = "yes" ]; then
			sed -i -e "s:\(SITELIBEXP = \).*:\1${sitelibexp}:" \
				-e "s:\(SITEARCHEXP = \).*:\1${sitearchexp}:" \
				-e "s:\(INSTALLVENDORLIB = \).*:\1${D}${datadir}/perl5:" \
				-e "s:\(INSTALLVENDORARCH = \).*:\1${D}${libdir}/perl5:" \
				-e "s:\(LDDLFLAGS.*\)${STAGING_LIBDIR_NATIVE}:\1${STAGING_LIBDIR}:" \
				Makefile
		else
			sed -i -e "s:\(SITELIBEXP = \).*:\1${sitelibexp}:" \
				-e "s:\(SITEARCHEXP = \).*:\1${sitearchexp}:" \
				-e "s:\(INSTALLVENDORLIB = \).*:\1${D}${libdir}/perl5/site_perl/${version}:" \
				-e "s:\(INSTALLVENDORARCH = \).*:\1${D}${libdir}/perl5/site_perl/${version}:" \
				-e "s:\(LDDLFLAGS.*\)${STAGING_LIBDIR_NATIVE}:\1${STAGING_LIBDIR}:" \
				Makefile
		fi
	fi
}

cpan_do_compile () {
	if [ "${IS_NEW_PERL}" = "yes" ]; then
		oe_runmake PASTHRU_INC="${CFLAGS}" CCFLAGS="${CFLAGS}" LD="${CCLD}"
	else
		# You must use gcc to link on sh
		OPTIONS=""
		if test ${TARGET_ARCH} = "sh3" -o ${TARGET_ARCH} = "sh4"; then
			OPTIONS="LD=${TARGET_ARCH}-${TARGET_OS}-gcc"
		fi
		if test ${TARGET_ARCH} = "powerpc" ; then
			OPTIONS="LD=${TARGET_ARCH}-${TARGET_OS}-gcc"
		fi
		oe_runmake PASTHRU_INC="${CFLAGS}" CCFLAGS="${CFLAGS}" $OPTIONS
	fi
}

cpan_do_install () {
	if [ ${@is_target(d)} = "yes" ]; then
		oe_runmake install_vendor
	fi
}

cpan_do_stage () {
	if [ ${@is_target(d)} = "no" ]; then
		oe_runmake install_vendor
	fi
}
								

EXPORT_FUNCTIONS do_configure do_compile do_install do_stage
