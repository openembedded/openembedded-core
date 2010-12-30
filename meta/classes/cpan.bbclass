#
# This is for perl modules that use the old Makefile.PL build system
#
inherit cpan-base

EXTRA_CPANFLAGS ?= ""

# Env var which tells perl if it should use host (no) or target (yes) settings
export PERLCONFIGTARGET = "${@is_target(d)}"

# Env var which tells perl where the perl include files are
export PERL_INC = "${STAGING_LIBDIR}/perl/${@get_perl_version(d)}/CORE"
export PERL_LIB = "${STAGING_LIBDIR}/perl/${@get_perl_version(d)}"
export PERL_ARCHLIB = "${STAGING_LIBDIR}/perl/${@get_perl_version(d)}"

cpan_do_configure () {
	export PERL5LIB="${PERL_ARCHLIB}"
	yes '' | perl Makefile.PL ${EXTRA_CPANFLAGS}
	if [ "${BUILD_SYS}" != "${HOST_SYS}" ]; then
		. ${STAGING_LIBDIR}/perl/config.sh
		# Use find since there can be a Makefile generated for each Makefile.PL
		for f in `find -name Makefile.PL`; do
			f2=`echo $f | sed -e 's/.PL//'`
			sed -i -e "s:\(PERL_ARCHLIB = \).*:\1${PERL_ARCHLIB}:" \
			$f2
		done
	fi
}

cpan_do_compile () {
	oe_runmake PASTHRU_INC="${CFLAGS}" CCFLAGS="${CFLAGS}" LD="${CCLD}"
}

cpan_do_install () {
	oe_runmake DESTDIR="${D}" install_vendor
}

EXPORT_FUNCTIONS do_configure do_compile do_install
