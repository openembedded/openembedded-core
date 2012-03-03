#
# This is for perl modules that use the new Build.PL build system
#
inherit cpan-base

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
	if [ ${@is_target(d)} == "yes" ]; then
		# build for target
		. ${STAGING_LIBDIR}/perl/config.sh

			perl Build.PL --installdirs vendor \
				--destdir ${D} \
				--install_path lib="${datadir}/perl" \
				--install_path arch="${libdir}/perl" \
				--install_path script=${bindir} \
				--install_path bin=${bindir} \
				--install_path bindoc=${mandir}/man1 \
				--install_path libdoc=${mandir}/man3
	else
		# build for host
		perl Build.PL --installdirs site --destdir ${D}
	fi
}

cpan_build_do_compile () {
        perl Build
}

cpan_build_do_install () {
	perl Build install
}

EXPORT_FUNCTIONS do_configure do_compile do_install
