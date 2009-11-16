DESCRIPTION = "Perl is a popular scripting language."
HOMEPAGE = "http://www.perl.org/"
LICENSE = "Artistic|GPL"
SECTION = "devel"
PRIORITY = "optional"
DEPENDS = "virtual/db perl-native"

SRC_URI = "ftp://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
	file://Makefile.patch;patch=1 \
	file://config.sh-arm-linux.patch;patch=1 \
	file://libperl-5.8.3-create-libperl-soname.patch;patch=1;pnum=0 \
	file://Makefile.SH.patch"

HOSTPERL="${STAGING_BINDIR_NATIVE}/perl${PV}"

do_compile() {
	sed -i -e 's|/usr/include|${STAGING_INCDIR}|g' ext/Errno/Errno_pm.PL
	cd Cross
	# You must use gcc to link on sh
	OPTIONS=""
	if test ${TARGET_ARCH} = "sh3" -o ${TARGET_ARCH} = "sh4"; then
		OPTIONS="LD=${TARGET_SYS}-gcc"
	fi
	# You must use gcc to link on powerpc also 
	if test ${TARGET_ARCH} = "powerpc" ; then 
		OPTIONS="LD=${TARGET_SYS}-gcc" 
	fi 

	oe_runmake perl $OPTIONS
}

do_install() {
	oe_runmake install
	mv ${D}/${libdir}/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}/CORE/libperl.so ${D}/${libdir}/libperl.so.${PV}
	( cd ${D}/usr/bin/; rm -f perl; ln -s perl${PV} perl )
}

do_stage() {
	install -d ${STAGING_LIBDIR}/perl/
	install config.sh ${STAGING_LIBDIR}/perl/
}

PACKAGES_DYNAMIC = "perl-module-*"

python populate_packages_prepend () {
	libdir = bb.data.expand('${libdir}/perl5/${PV}', d)
	archlibdir =  bb.data.expand('${libdir}/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}', d)
	do_split_packages(d, archlibdir, 'auto/(.*)(?!\.debug)/', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
	do_split_packages(d, archlibdir, '(.*)\.(pm|pl)', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
	do_split_packages(d, libdir, '(.*)\.(pm|pl)', 'perl-module-%s', 'perl module %s', recursive=True, allow_dirs=False, match_path=True)
}

PACKAGES = "perl-dbg perl perl-misc perl-lib perl-dev perl-pod"
FILES_${PN} = "/usr/bin/perl /usr/bin/perl${PV}"
FILES_${PN}-lib = "/usr/lib/libperl.so*"
FILES_${PN}-dev = "/usr/lib/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}/CORE/"
FILES_${PN}-pod = "/usr/lib/perl5/${PV}/pod"
FILES_perl-misc = "/usr/bin/*"
FILES_${PN}-dbg += " \
        ${libdir}/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}/auto/*/.debug \
        ${libdir}/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}/auto/*/*/.debug \
        ${libdir}/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}/auto/*/*/*/.debug"

