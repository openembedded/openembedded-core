SUMMARY = "Perl (Pathologically Eclectic Rubbish Lister)"
DESCRIPTION = "Perl is a high-level, general-purpose, interpreted, dynamic programming language.  It was \
originally written as a general-purpose scripting language to make report processing easier.  This makes perl \
especially good at handling text."
HOMEPAGE = "http://www.perl.org/"
SECTION = "devel"
LICENSE = "Artistic|GPL"
LIC_FILES_CHKSUM = "file://Copying;md5=2b4c6ffbcfcbdee469f02565f253d81a \
		    file://Artistic;md5=f921793d03cc6d63ec4b15e9be8fd3f8"
PRIORITY = "optional"
# We need gnugrep (for -I)
DEPENDS = "virtual/db perl-native grep-native"
DEPENDS += "gdbm"
PR = "r22"

# Major part of version
PVM = "5.8"

SRC_URI = "ftp://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
        file://Makefile.patch;patch=1 \
        file://Makefile.SH.patch;patch=1 \
        file://makedepend-dash.patch;patch=1 \
        file://installperl.patch;patch=1 \
        file://perl-dynloader.patch;patch=1 \
        file://perl-moreconfig.patch;patch=1 \
        file://letgcc-find-errno.patch;patch=1 \
        file://generate-sh.patch;patch=1 \
        file://perl-5.8.8-gcc-4.2.patch;patch=1 \
        file://09_fix_installperl.patch;patch=1 \
        file://52_debian_extutils_hacks.patch;patch=1 \
        file://53_debian_mod_paths.patch;patch=1 \
        file://54_debian_perldoc-r.patch;patch=1 \
        file://58_debian_cpan_config_path.patch;patch=1 \
        file://60_debian_libnet_config_path.patch;patch=1 \
        file://62_debian_cpan_definstalldirs.patch;patch=1 \
        file://64_debian_enc2xs_inc.patch;patch=1 \
        file://asm-pageh-fix.patch;patch=1 \
        file://native-perlinc.patch;patch=1 \
	file://perl-enable-gdbm.patch;patch=1 \
        file://config.sh \
        file://config.sh-32 \
        file://config.sh-32-le \
        file://config.sh-32-be \
        file://config.sh-64 \
        file://config.sh-64-le \
        file://config.sh-64-be"

SRC_URI[md5sum] = "b8c118d4360846829beb30b02a6b91a7"
SRC_URI[sha256sum] = "e15d499321e003d12ed183601e37ee7ba5f64b278d1de30149ce01bd4a3f234d"

inherit siteinfo

# Where to find the native perl
HOSTPERL = "${STAGING_BINDIR_NATIVE}/perl${PV}"

# Where to find .so files - use the -native versions not those from the target build
export PERLHOSTLIB = "${STAGING_LIBDIR_NATIVE}/perl/${PV}/"

do_configure() {
        # Make hostperl in build directory be the native perl
        ln -s ${HOSTPERL} hostperl

        # Do our work in the cross subdir
        cd Cross

        # Generate configuration
        rm -f config.sh-${TARGET_ARCH}-${TARGET_OS}
        for i in ${WORKDIR}/config.sh \
                 ${WORKDIR}/config.sh-${@siteinfo_get_bits(d)} \
                 ${WORKDIR}/config.sh-${@siteinfo_get_bits(d)}-${@siteinfo_get_endianess(d)}; do
            cat $i >> config.sh-${TARGET_ARCH}-${TARGET_OS}
        done

        # Fixups for uclibc
        if [ "${TARGET_OS}" = "linux-uclibc" -o "${TARGET_OS}" = "linux-uclibcgnueabi" ]; then
                sed -i -e "s,\(d_crypt_r=\)'define',\1'undef',g" \
                       -e "s,\(crypt_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_getnetbyname_r=\)'define',\1'undef',g" \
                       -e "s,\(getnetbyname_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_getnetbyaddr_r=\)'define',\1'undef',g" \
                       -e "s,\(getnetbyaddr_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_getnetent_r=\)'define',\1'undef',g" \
                       -e "s,\(getnetent_r_proto=\)'\w+',\1'0',g" \
                       -e "s,\(d_sockatmark=\)'define',\1'undef',g" \
                       -e "s,\(d_sockatmarkproto=\)'\w+',\1'0',g" \
                    config.sh-${TARGET_ARCH}-${TARGET_OS}
        fi

        # Update some paths in the configuration
        sed -i -e 's,@LIBDIR@,${libdir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               -e 's,@MANDIR@,${mandir},g' \
               -e 's,@PREFIX@,${prefix},g' \
               -e 's,@DATADIR@,${datadir},g' \
               -e 's,@ARCH@,${TARGET_ARCH}-${TARGET_OS},g' \
               -e "s%/usr/include/%${STAGING_INCDIR}/%g" \
	       -e 's,/usr/,${exec_prefix}/,g' \
            config.sh-${TARGET_ARCH}-${TARGET_OS}


         # These are strewn all over the source tree
        for foo in `grep -I -m1 \/usr\/include\/.*\\.h ${WORKDIR}/* -r | cut -f 1 -d ":"` ; do
            echo Fixing: $foo
            sed -e "s%/usr/include/%${STAGING_INCDIR}/%g" -i $foo
        done

        rm -f config
        echo "ARCH = ${TARGET_ARCH}" > config
        echo "OS = ${TARGET_OS}" >> config
}

do_compile() {
        if test "${MACHINE}" != "native"; then
            sed -i -e 's|/usr/include|${STAGING_INCDIR}|g' ext/Errno/Errno_pm.PL
        fi
        cd Cross
        oe_runmake perl LD="${CCLD}"
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install

        # Add perl pointing at current version
        ln -sf perl${PV} ${D}${bindir}/perl

        # Fix up versioned directories
        mv ${D}/${libdir}/perl/${PVM} ${D}/${libdir}/perl/${PV}
        mv ${D}/${datadir}/perl/${PVM} ${D}/${datadir}/perl/${PV}
        ln -sf ${PV} ${D}/${libdir}/perl/${PVM}
        ln -sf ${PV} ${D}/${datadir}/perl/${PVM}

        # Remove unwanted file
        rm -f ${D}/${libdir}/perl/${PV}/.packlist

        # Fix up shared library
        mv -f ${D}/${libdir}/perl/${PV}/CORE/libperl.so ${D}/${libdir}/libperl.so.${PV}
        ln -sf libperl.so.${PV} ${D}/${libdir}/libperl.so.5

        # target config, used by cpan.bbclass to extract version information
        install config.sh ${D}${libdir}/perl/

        install -d ${D}${datadir}/perl/${PV}/ExtUtils
	install lib/ExtUtils/typemap ${D}${datadir}/perl/${PV}/ExtUtils/
	
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
                   -e "s,${STAGING_BINDIR_NATIVE}/,,g" \
                ${PKGD}${bindir}/h2xs \
                ${PKGD}${bindir}/h2ph \
                ${PKGD}${datadir}/perl/${PV}/pod/*.pod \
                ${PKGD}${datadir}/perl/${PV}/cacheout.pl \
                ${PKGD}${datadir}/perl/${PV}/FileCache.pm \
                ${PKGD}${libdir}/perl/config.sh \
                ${PKGD}${libdir}/perl/${PV}/Config.pm \
                ${PKGD}${libdir}/perl/${PV}/Config_heavy.pl \
                ${PKGD}${libdir}/perl/${PV}/CORE/perl.h \
                ${PKGD}${libdir}/perl/${PV}/CORE/pp.h
}

PACKAGES = "perl-dbg perl perl-misc perl-lib perl-dev perl-pod perl-doc"
FILES_${PN} = "${bindir}/perl ${bindir}/perl${PV}"
FILES_${PN}-lib = "${libdir}/libperl.so* ${libdir}/perl/${PVM} ${datadir}/perl/${PVM}"
FILES_${PN}-dev = "${libdir}/perl/${PV}/CORE"
FILES_${PN}-pod = "${datadir}/perl/${PV}/pod \
                   ${datadir}/perl/${PV}/*/*.pod \
                   ${datadir}/perl/${PV}/*/*/*.pod \
                   ${libdir}/perl/${PV}/*.pod"
FILES_perl-misc = "${bindir}/*"
FILES_${PN}-dbg += "${libdir}/perl/${PV}/auto/*/.debug \
                    ${libdir}/perl/${PV}/auto/*/*/.debug \
                    ${libdir}/perl/${PV}/auto/*/*/*/.debug \
                    ${datadir}/perl/${PV}/auto/*/.debug \
                    ${datadir}/perl/${PV}/auto/*/*/.debug \
                    ${datadir}/perl/${PV}/auto/*/*/*/.debug \
                    ${libdir}/perl/${PV}/CORE/.debug"
FILES_${PN}-doc = "${datadir}/perl/${PV}/*/*.txt \
                   ${datadir}/perl/${PV}/*/*/*.txt \
                   ${datadir}/perl/${PV}/Net/*.eg \
                   ${datadir}/perl/${PV}/CGI/eg \
                   ${datadir}/perl/${PV}/ExtUtils/PATCHING \
                   ${datadir}/perl/${PV}/ExtUtils/NOTES \
                   ${datadir}/perl/${PV}/ExtUtils/typemap \
                   ${datadir}/perl/${PV}/ExtUtils/MANIFEST.SKIP \
                   ${datadir}/perl/${PV}/CPAN/SIGNATURE \
                   ${datadir}/perl/${PV}/CPAN/PAUSE2003.pub \
                   ${datadir}/perl/${PV}/B/assemble \
                   ${datadir}/perl/${PV}/B/makeliblinks \
                   ${datadir}/perl/${PV}/B/disassemble \
                   ${datadir}/perl/${PV}/B/cc_harness \
                   ${datadir}/perl/${PV}/ExtUtils/xsubpp \
                   ${datadir}/perl/${PV}/Encode/encode.h \
                   ${datadir}/perl/${PV}/unicore/mktables \
                   ${datadir}/perl/${PV}/unicore/mktables.lst \
                   ${datadir}/perl/${PV}/unicore/version"

RPROVIDES_perl-lib = "perl-lib"

# Create a perl-modules package recommending all the other perl
# packages (actually the non modules packages and not created too)
ALLOW_EMPTY_perl-modules = "1"
PACKAGES_append = " perl-modules "
RRECOMMENDS_perl-modules = "${@bb.data.getVar('PACKAGES', d, 1).replace('perl-modules ', '').replace('perl-dbg ', '').replace('perl-misc ', '').replace('perl-dev ', '').replace('perl-pod ', '').replace('perl-doc ', '')}"

python populate_packages_prepend () {
        libdir = bb.data.expand('${libdir}/perl/${PV}', d)
        do_split_packages(d, libdir, 'auto/(.*)(?!\.debug)/', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
        do_split_packages(d, libdir, '(.*)\.(pm|pl|e2x)', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
        datadir = bb.data.expand('${datadir}/perl/${PV}', d)
        do_split_packages(d, datadir, 'auto/(.*)(?!\.debug)/', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
        do_split_packages(d, datadir, '(.*)\.(pm|pl|e2x)', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
}

PACKAGES_DYNAMIC = "perl-module-*"

require perl-rdepends_${PV}.inc
require perl-rprovides.inc

PARALLEL_MAKE = ""
