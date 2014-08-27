#
# This is for perl modules that use the new Build.PL build system
#
inherit cpan-base perlnative

EXTRA_CPAN_BUILD_FLAGS ?= ""

# Env var which tells perl if it should use host (no) or target (yes) settings
export PERLCONFIGTARGET = "${@is_target(d)}"
export PERL_ARCHLIB = "${STAGING_LIBDIR}${PERL_OWN_DIR}/perl/${@get_perl_version(d)}"
export LD = "${CCLD}"

#
# We also need to have built libmodule-build-perl-native for
# everything except libmodule-build-perl-native itself (which uses
# this class, but uses itself as the provider of
# libmodule-build-perl)
#
def cpan_build_dep_prepend(d):
	if d.getVar('CPAN_BUILD_DEPS', True):
		return ''
	pn = d.getVar('PN', True)
	if pn in ['libmodule-build-perl', 'libmodule-build-perl-native']:
		return ''
	return 'libmodule-build-perl-native '

DEPENDS_prepend = "${@cpan_build_dep_prepend(d)}"

cpan_build_do_configure () {
	if [ "${@is_target(d)}" = "yes" ]; then
		# build for target
		. ${STAGING_LIBDIR}/perl/config.sh
	fi

	perl Build.PL --installdirs vendor \
				--destdir ${D} \
				--install_path arch="${libdir}/perl" \
				--install_path script=${bindir} \
				--install_path bin=${bindir} \
				--install_path bindoc=${mandir}/man1 \
				--install_path libdoc=${mandir}/man3 \
                                ${EXTRA_CPAN_BUILD_FLAGS}
}

cpan_build_do_compile () {
        perl Build
}

cpan_build_do_install () {
	perl Build install
}

EXPORT_FUNCTIONS do_configure do_compile do_install
