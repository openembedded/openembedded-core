#
# This is for perl modules that use the new Build.PL build system
#
inherit cpan-base

INHIBIT_NATIVE_STAGE_INSTALL = "1"

#
# We also need to have built libmodule-build-perl-native for
# everything except libmodule-build-perl-native itself (which uses
# this class, but uses itself as the provider of
# libmodule-build-perl)
#
def cpan_build_dep_prepend(d):
	import bb;
	if bb.data.getVar('CPAN_BUILD_DEPS', d, 1):
		return ''
	pn = bb.data.getVar('PN', d, 1)
	if pn in ['libmodule-build-perl', 'libmodule-build-perl-native']:
		return ''
	return 'libmodule-build-perl-native '

DEPENDS_prepend = "${@cpan_build_dep_prepend(d)}"

cpan_build_do_configure () {
	if [ ${@is_target(d)} == "yes" ]; then
		# build for target
		. ${STAGING_DIR}/${TARGET_SYS}/perl/config.sh
		if [ "${IS_NEW_PERL}" = "yes" ]; then
			perl Build.PL --installdirs vendor \
				--destdir ${D} \
				--install_path lib="${datadir}/perl5" \
				--install_path arch="${libdir}/perl5" \
				--install_path script=${bindir} \
				--install_path bin=${bindir} \
				--install_path bindoc=${mandir}/man1 \
				--install_path libdoc=${mandir}/man3
		else
			perl Build.PL --installdirs vendor \
				--destdir ${D} \
				--install_path lib="${libdir}/perl5/site_perl/${version}" \
				--install_path arch="${libdir}/perl5/site_perl/${version}/${TARGET_SYS}" \
				--install_path script=${bindir} \
				--install_path bin=${bindir} \
				--install_path bindoc=${mandir}/man1 \
				--install_path libdoc=${mandir}/man3
		fi
	else
		# build for host
		perl Build.PL --installdirs site
	fi
}

cpan_build_do_compile () {
        perl Build
}

cpan_build_do_install () {
	if [ ${@is_target(d)} == "yes" ]; then
		perl Build install
	fi
}

do_stage_append () {
	if [ ${@is_target(d)} == "no" ]; then
		perl Build install
	fi
}

EXPORT_FUNCTIONS do_configure do_compile do_install
