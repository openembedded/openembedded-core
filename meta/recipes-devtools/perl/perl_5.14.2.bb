DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
SECTION = "devel"
LICENSE = "Artistic-1.0 | GPL-1.0"
LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
		    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"
# We need gnugrep (for -I)
DEPENDS = "virtual/db grep-native"
DEPENDS += "gdbm zlib"
PR = "r6"

# 5.10.1 has Module::Build built-in
PROVIDES += "libmodule-build-perl"

SRC_URI = "http://www.cpan.org/src/5.0/perl-${PV}.tar.gz \
	file://debian/arm_thread_stress_timeout.diff \
	file://debian/cpan_definstalldirs.diff \
	file://debian/db_file_ver.diff \
	file://debian/doc_info.diff \
	file://debian/enc2xs_inc.diff \
	file://debian/errno_ver.diff \
	file://debian/libperl_embed_doc.diff \
	file://debian/fixes/respect_umask.diff \
	file://debian/writable_site_dirs.diff \
	file://debian/extutils_set_libperl_path.diff \
	file://debian/no_packlist_perllocal.diff \
	file://debian/prefix_changes.diff \
	file://debian/fakeroot.diff \
	file://debian/instmodsh_doc.diff \
	file://debian/ld_run_path.diff \
	file://debian/libnet_config_path.diff \
	file://debian/m68k_thread_stress.diff \
	file://debian/mod_paths.diff \
	file://debian/module_build_man_extensions.diff \
	file://debian/prune_libs.diff \
	file://debian/fixes/net_smtp_docs.diff \
	file://debian/perlivp.diff \
	file://debian/disable-zlib-bundling.diff \
	file://debian/cpanplus_definstalldirs.diff \
	file://debian/cpanplus_config_path.diff \
	file://debian/deprecate-with-apt.diff \
	file://debian/fixes/hurd-ccflags.diff \
	file://debian/squelch-locale-warnings.diff \
	file://debian/skip-upstream-git-tests.diff \
	file://debian/fixes/extutils-cbuilder-cflags.diff \
	file://debian/fixes/module-build-home-directory.diff \
	file://debian/fixes/h2ph-multiarch.diff \
	file://debian/fixes/index-tainting.diff \
	file://debian/skip-kfreebsd-crash.diff \
	file://debian/fixes/document_makemaker_ccflags.diff \
	file://debian/fixes/sys-syslog-socket-timeout-kfreebsd.patch \
	file://debian/fixes/hurd-hints.diff \
	file://debian/fixes/pod_fixes.diff \
	file://debian/find_html2text.diff \
	\
        file://Makefile.patch \
        file://Makefile.SH.patch \
        file://installperl.patch \
        file://perl-dynloader.patch \
        file://perl-moreconfig.patch \
        file://letgcc-find-errno.patch \
        file://generate-sh.patch \
        file://09_fix_installperl.patch \
        file://native-perlinc.patch \
        file://perl-enable-gdbm.patch \
        file://cross-generate_uudmap.patch \
	file://fix_bad_rpath.patch \
	\
        file://config.sh \
        file://config.sh-32 \
        file://config.sh-32-le \
        file://config.sh-32-be \
        file://config.sh-64 \
        file://config.sh-64-le \
        file://config.sh-64-be"
#	file://debian/fakeroot.diff

SRC_URI[md5sum] = "3306fbaf976dcebdcd49b2ac0be00eb9"
SRC_URI[sha256sum] = "6488359573bd7d41761bf935f66f827dc220fb3df961ef9b775d51fbd66548d3"

inherit perlnative siteinfo

# Where to find the native perl
HOSTPERL = "${STAGING_BINDIR_NATIVE}/perl-native/perl${PV}"

# Where to find .so files - use the -native versions not those from the target build
export PERLHOSTLIB = "${STAGING_LIBDIR_NATIVE}/perl-native/perl/${PV}/"

# LDFLAGS for shared libraries
export LDDLFLAGS = "${LDFLAGS} -shared"

# We're almost Debian, aren't we?
CFLAGS += "-DDEBIAN"

do_nolargefile() {
	sed -i -e "s,\(uselargefiles=\)'define',\1'undef',g" \
		-e "s,\(d_readdir64_r=\)'define',\1'undef',g" \
		-e "s,\(readdir64_r_proto=\)'\w+',\1'0',g" \
		-e "/ccflags_uselargefiles/d" \
		-e "s/-Duselargefiles//" \
		-e "s/-D_FILE_OFFSET_BITS=64//" \
		-e "s/-D_LARGEFILE_SOURCE//" \
		${S}/Cross/config.sh-${TARGET_ARCH}-${TARGET_OS}
}

do_configure() {
        # Make hostperl in build directory be the native perl
        ln -sf ${HOSTPERL} hostperl

        # Do our work in the cross subdir
        cd Cross

        # Generate configuration
        rm -f config.sh-${TARGET_ARCH}-${TARGET_OS}
        for i in ${WORKDIR}/config.sh \
                 ${WORKDIR}/config.sh-${SITEINFO_BITS} \
                 ${WORKDIR}/config.sh-${SITEINFO_BITS}-${SITEINFO_ENDIANNESS}; do
            cat $i >> config.sh-${TARGET_ARCH}-${TARGET_OS}
        done

        # Fixups for uclibc
        if [ "${TARGET_OS}" = "linux-uclibc" -o "${TARGET_OS}" = "linux-uclibceabi" ]; then
                sed -i -e "s,\(d_crypt_r=\)'define',\1'undef',g" \
                       -e "s,\(d_futimes=\)'define',\1'undef',g" \
                       -e "s,\(crypt_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_getnetbyname_r=\)'define',\1'undef',g" \
                       -e "s,\(getnetbyname_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_getnetbyaddr_r=\)'define',\1'undef',g" \
                       -e "s,\(getnetbyaddr_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_getnetent_r=\)'define',\1'undef',g" \
                       -e "s,\(getnetent_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_sockatmark=\)'define',\1'undef',g" \
                       -e "s,\(d_sockatmarkproto=\)'\w+',\1'0',g" \
                       -e "s,\(d_eaccess=\)'define',\1'undef',g" \
                       -e "s,\(d_stdio_ptr_lval=\)'define',\1'undef',g" \
                       -e "s,\(d_stdio_ptr_lval_sets_cnt=\)'define',\1'undef',g" \
                       -e "s,\(d_stdiobase=\)'define',\1'undef',g" \
                       -e "s,\(d_stdstdio=\)'define',\1'undef',g" \
                       -e "s,-fstack-protector,-fno-stack-protector,g" \
                    config.sh-${TARGET_ARCH}-${TARGET_OS}
        fi

	${@base_contains('DISTRO_FEATURES', 'largefile', '', 'do_nolargefile', d)}

        # Update some paths in the configuration
        sed -i -e 's,@DESTDIR@,${prefix},g' \
               -e 's,@ARCH@-thread-multi,,g' \
               -e 's,@ARCH@,${TARGET_ARCH}-${TARGET_OS},g' \
               -e "s%/usr/include%${STAGING_INCDIR}%g" \
	       -e 's,/usr/lib/,${libdir}/,g' \
	       -e 's,/usr/,${exec_prefix}/,g' \
	       -e 's,/perl5,/perl,g' \
            config.sh-${TARGET_ARCH}-${TARGET_OS}

	case "${TARGET_ARCH}" in
		x86_64 | powerpc | s390)
			sed -i -e "s,\(need_va_copy=\)'undef',\1'define',g" \
				config.sh-${TARGET_ARCH}-${TARGET_OS}
			;;
		arm)
			sed -i -e "s,\(d_u32align=\)'undef',\1'define',g" \
				config.sh-${TARGET_ARCH}-${TARGET_OS}
			;;
	esac
        # These are strewn all over the source tree
        for foo in `grep -I --exclude="*.patch" --exclude="*.diff" --exclude="*.pod" --exclude="README*" -m1 "/usr/include/.*\.h" ${WORKDIR}/* -r -l` ${S}/utils/h2xs.PL ; do
            echo Fixing: $foo
            sed -e 's|\([ "^'\''I]\+\)/usr/include/|\1${STAGING_INCDIR}/|g' -i $foo
        done

        rm -f config
        echo "ARCH = ${TARGET_ARCH}" > config
        echo "OS = ${TARGET_OS}" >> config
}

do_compile() {
        # Fix to avoid recursive substitution of path
        sed -i -e "s|\([ \"\']\+\)/usr/include|\1${STAGING_INCDIR}|g" ext/Errno/Errno_pm.PL
        sed -i -e "s|\([ \"\']\+\)/usr/include|\1${STAGING_INCDIR}|g" cpan/Compress-Raw-Zlib/config.in
        sed -i -e 's|/usr/lib|""|g' cpan/Compress-Raw-Zlib/config.in
        sed -i -e 's|SYSROOTLIB|${STAGING_LIBDIR}|g' cpan/ExtUtils-MakeMaker/lib/ExtUtils/Liblist/Kid.pm

        cd Cross
        oe_runmake perl LD="${CCLD}"
}

do_install() {
	oe_runmake install DESTDIR=${D}
        # Add perl pointing at current version
        ln -sf perl${PV} ${D}${bindir}/perl

        ln -sf perl ${D}/${libdir}/perl5

        # Remove unwanted file and empty directories
        rm -f ${D}/${libdir}/perl/${PV}/.packlist
	rmdir ${D}/${libdir}/perl/site_perl/${PV}
	rmdir ${D}/${libdir}/perl/site_perl

        # Fix up shared library
        mv ${D}/${libdir}/perl/${PV}/CORE/libperl.so ${D}/${libdir}/libperl.so.${PV}
        ln -sf libperl.so.${PV} ${D}/${libdir}/libperl.so.5

        # target config, used by cpan.bbclass to extract version information
        install config.sh ${D}${libdir}/perl

        ln -s Config_heavy.pl ${D}${libdir}/perl/${PV}/Config_heavy-target.pl

}

PACKAGE_PREPROCESS_FUNCS += "perl_package_preprocess"

perl_package_preprocess () {
        # Fix up installed configuration
        sed -i -e "s,${D},,g" \
               -e "s,-isystem${STAGING_INCDIR} ,,g" \
               -e "s,${STAGING_LIBDIR},${libdir},g" \
               -e "s,${STAGING_BINDIR},${bindir},g" \
               -e "s,${STAGING_INCDIR},${includedir},g" \
               -e "s,${STAGING_BINDIR_NATIVE}/perl-native/,${bindir}/,g" \
               -e "s,${STAGING_BINDIR_NATIVE}/,,g" \
            ${PKGD}${bindir}/h2xs \
            ${PKGD}${bindir}/h2ph \
            ${PKGD}${bindir}/pod2man \
            ${PKGD}${bindir}/pod2text \
            ${PKGD}${bindir}/pod2usage \
            ${PKGD}${bindir}/podchecker \
            ${PKGD}${bindir}/podselect \
            ${PKGD}${libdir}/perl/${PV}/pod/*.pod \
            ${PKGD}${libdir}/perl/${PV}/cacheout.pl \
            ${PKGD}${libdir}/perl/${PV}/FileCache.pm \
            ${PKGD}${libdir}/perl/config.sh \
            ${PKGD}${libdir}/perl/${PV}/Config.pm \
            ${PKGD}${libdir}/perl/${PV}/Config_heavy.pl \
            ${PKGD}${libdir}/perl/${PV}/CORE/perl.h \
            ${PKGD}${libdir}/perl/${PV}/CORE/pp.h
}

PACKAGES = "perl-dbg perl perl-misc perl-dev perl-pod perl-doc perl-lib \
            perl-module-cpan perl-module-cpanplus perl-module-unicore"
FILES_${PN} = "${bindir}/perl ${bindir}/perl${PV}"
FILES_${PN}-dev = "${libdir}/perl/${PV}/CORE"
FILES_${PN}-lib = "${libdir}/libperl.so* \
                   ${libdir}/perl5 \
                   ${libdir}/perl/config.sh \
                   ${libdir}/perl/${PV}/Config_heavy.pl \
                   ${libdir}/perl/${PV}/Config_heavy-target.pl"
FILES_${PN}-pod = "${libdir}/perl/${PV}/pod \
		   ${libdir}/perl/${PV}/*.pod \
                   ${libdir}/perl/${PV}/*/*.pod \
                   ${libdir}/perl/${PV}/*/*/*.pod "
FILES_perl-misc = "${bindir}/*"
FILES_${PN}-dbg += "${libdir}/perl/${PV}/auto/*/.debug \
                    ${libdir}/perl/${PV}/auto/*/*/.debug \
                    ${libdir}/perl/${PV}/auto/*/*/*/.debug \
                    ${libdir}/perl/${PV}/CORE/.debug \
                    ${libdir}/perl/${PV}/*/.debug \
                    ${libdir}/perl/${PV}/*/*/.debug \
                    ${libdir}/perl/${PV}/*/*/*/.debug "
FILES_${PN}-doc = "${libdir}/perl/${PV}/*/*.txt \
                   ${libdir}/perl/${PV}/*/*/*.txt \
                   ${libdir}/perl/${PV}/B/assemble \
                   ${libdir}/perl/${PV}/B/cc_harness \
                   ${libdir}/perl/${PV}/B/disassemble \
                   ${libdir}/perl/${PV}/B/makeliblinks \
                   ${libdir}/perl/${PV}/CGI/eg \
                   ${libdir}/perl/${PV}/CPAN/PAUSE2003.pub \
                   ${libdir}/perl/${PV}/CPAN/SIGNATURE \
		   ${libdir}/perl/${PV}/CPANPLUS/Shell/Default/Plugins/HOWTO.pod \
                   ${libdir}/perl/${PV}/Encode/encode.h \
                   ${libdir}/perl/${PV}/ExtUtils/MANIFEST.SKIP \
                   ${libdir}/perl/${PV}/ExtUtils/NOTES \
                   ${libdir}/perl/${PV}/ExtUtils/PATCHING \
                   ${libdir}/perl/${PV}/ExtUtils/typemap \
                   ${libdir}/perl/${PV}/ExtUtils/xsubpp \
		   ${libdir}/perl/${PV}/ExtUtils/Changes_EU-Install \
                   ${libdir}/perl/${PV}/Net/*.eg \
                   ${libdir}/perl/${PV}/unicore/mktables \
                   ${libdir}/perl/${PV}/unicore/mktables.lst \
                   ${libdir}/perl/${PV}/unicore/version "

FILES_perl-module-cpan += "${libdir}/perl/${PV}/CPAN \
                           ${libdir}/perl/${PV}/CPAN.pm"
FILES_perl-module-cpanplus += "${libdir}/perl/${PV}/CPANPLUS \
                               ${libdir}/perl/${PV}/CPANPLUS.pm"
FILES_perl-module-unicore += "${libdir}/perl/${PV}/unicore"

# Create a perl-modules package recommending all the other perl
# packages (actually the non modules packages and not created too)
ALLOW_EMPTY_perl-modules = "1"
PACKAGES_append = " perl-modules "
RRECOMMENDS_perl-modules = "${@d.getVar('PACKAGES', True).replace('${PN}-modules ', '').replace('${PN}-dbg ', '').replace('${PN}-misc ', '').replace('${PN}-dev ', '').replace('${PN}-pod ', '').replace('${PN}-doc ', '')}"

python populate_packages_prepend () {
        libdir = d.expand('${libdir}/perl/${PV}')
        do_split_packages(d, libdir, 'auto/(Encode/.[^/]*)/.*', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True, prepend=False)
        do_split_packages(d, libdir, 'auto/([^/]*)/.*', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True, prepend=False)
        do_split_packages(d, libdir, 'Module/([^\/]*).*', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True, prepend=False)
        do_split_packages(d, libdir, '(^(?!(CPAN\/|CPANPLUS\/|Module\/|unicore\/|auto\/)[^\/]).*)\.(pm|pl|e2x)', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True, prepend=False)
}

PACKAGES_DYNAMIC = "perl-module-*"

RPROVIDES_perl-lib = "perl-lib"

require perl-rdepends_${PV}.inc
require perl-rprovides.inc
require perl-rprovides_${PV}.inc

SSTATE_SCAN_FILES += "*.pm *.pod *.h *.pl *.sh"

