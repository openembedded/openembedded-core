SUMMARY = "The RPM package management system"
DESCRIPTION = "The RPM Package Manager (RPM) is a powerful command line driven \
package management system capable of installing, uninstalling, \
verifying, querying, and updating software packages. Each software \
package consists of an archive of files along with information about \
the package like its version, a description, etc."

SUMMARY_${PN}-libs = "Libraries for manipulating RPM packages."
DESCRIPTION_${PN}-libs = "This package contains the RPM shared libraries."

SUMMARY_${PN}-dev = "Development files for manipulating RPM packages."
DESCRIPTION_${PN}-dev = "This package contains the RPM C library and header files. These \
development files will simplify the process of writing programs that \
manipulate RPM packages and databases. These files are intended to \
simplify the process of creating graphical package managers or any \
other tools that need an intimate knowledge of RPM packages in order \
to function."

SUMMARY_${PN}-common = "Common RPM paths, scripts, documentation and configuration."
DESCRIPTION_${PN}-common = "The rpm-common package contains paths, scripts, documentation \
and configuration common between RPM Package Manager."

SUMMARY_${PN}-build = "Scripts and executable programs used to build packages."
DESCRIPTION_${PN}-build = "The rpm-build packagec ontains the scripts and executable programs \
that are used to build packages using the RPM Package Manager."

SUMMARY_python-rpm = "Python bindings for apps which will manupulate RPM packages."
DESCRIPTION_python-rpm = "The rpm-python package contains a module that permits applications \
written in the Python programming language to use the interface \
supplied by the RPM Package Manager libraries."

SUMMARY_perl-module-rpm = "Perl bindings for apps which will manipulate RPM packages."
DESCRIPTION_perl-modules-rpm = "The perl-modules-rpm package contains a module that permits applications \
written in the Perl programming language to use the interface \
supplied by the RPM Package Manager libraries."

SUMMARY_perl-module-rpm-dev = "Development components for perl bindings"
DESCRIPTION_perl-modules-rpm-dev = "Development items such as man pages for use with the Perl \
language bindings."

HOMEPAGE = "http://rpm5.org/"
LICENSE = "LGPL 2.1"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = "bzip2 zlib db openssl elfutils expat libpcre attr acl popt ${extrarpmdeps}"
extrarpmdeps = "python perl"
extrarpmdeps_virtclass-native = "file-native"
PR = "r22"

# rpm2cpio is a shell script, which is part of the rpm src.rpm.  It is needed
# in order to extract the distribution SRPM into a format we can extract...
SRC_URI = "http://www.rpm5.org/files/rpm/rpm-5.4/rpm-5.4.0-0.20101229.src.rpm;extract=rpm-5.4.0.tar.gz \
	   file://perfile_rpmdeps.sh \
	   file://rpm-autogen.patch \
	   file://rpm-libsql-fix.patch \
	   file://header-include-fix.patch \
	   file://rpm-platform.patch \
	   file://rpm-showrc.patch \
	   file://rpm-nofsync.patch \
	   file://rpm-solvedb.patch \
	   file://rpm-tools-mtree-LDFLAGS.patch \
	   file://fprint-pointer-fix.patch \
	   file://rpm-fileclass.patch \
	   file://rpm-canonarch.patch \
	   file://rpm-no-loopmsg.patch \
	   file://pythondeps.sh \
	  "

#	   file://rpm-autoconf.patch \
#	   file://remove-compiled-tests.patch;apply=no \
#	  "

SRC_URI[md5sum] = "19c1a7f68d7765eeb7615c9c4e54e380"
SRC_URI[sha256sum] = "887e76218308b570c33c8c2fb10b5298b3afd5d602860d281befc85357b3b923"

inherit autotools gettext

acpaths = "-I ${S}/db/dist/aclocal -I ${S}/db/dist/aclocal_java"

# Specify the default rpm macros in terms of adjustable variables
rpm_macros = "%{_usrlibrpm}/macros:%{_usrlibrpm}/poky/macros:%{_usrlibrpm}/poky/%{_target}/macros:%{_etcrpm}/macros.*:%{_etcrpm}/macros:%{_etcrpm}/%{_target}/macros:~/.oerpmmacros"
rpm_macros_virtclass-native = "%{_usrlibrpm}/macros:%{_usrlibrpm}/poky/macros:%{_usrlibrpm}/poky/%{_target}/macros:~/.oerpmmacros"

# Configure values taken from rpm.spec
WITH_BZIP2 = "--with-bzip2"

WITH_XZ = "--with-xz=none"

WITH_Z = "--with-zlib $WITH_BZIP2 $WITH_XZ"

WITH_PYTHON = "	--with-python=${PYTHON_BASEVERSION} \
		--with-python-inc-dir=${STAGING_INCDIR}/python${PYTHON_BASEVERSION} \
		--with-python-lib-dir=${libdir}/python${PYTHON_BASEVERSION} \
		--without-pythonembed \
	      "

WITH_PYTHON_virtclass-native = " --without-python"

# Perl modules are not built, but they could be enabled fairly easily
# the perl module creation and installation would need to be patched.
# (currently has host perl contamination issues)
#WITH_PERL = "	--with-perl --without-perlembed"
WITH_PERL = "	--without-perl"

WITH_PERL_virtclass-native = " --without-perl"

WITH_DB = "--with-db --with-dbsql --without-db-tools-integrated --without-sqlite"

WITH_CRYPTO = "--with-beecrypt=internal --with-openssl --without-nss --without-gcrypt"

WITH_KEYUTILS = "--without-keyutils"
WITH_LIBELF = "--with-libelf"
WITH_SELINUX = "--without-selinux --without-sepol --without-semanage"
#WITH_NEON = "--with-neon=internal --without-libproxy --with-expat --without-gssapi"
WITH_NEON = "--without-neon --without-libproxy --without-expat --without-gssapi"

EXTRA_OECONF = "--verbose \
		--sysconfdir=/etc \
		${WITH_DB} \
		${WITH_Z} \
		--with-file \
		--with-path-magic=%{_usrlibrpm}/../../share/misc/magic \
		--without-lua \
		--without-tcl \
		--with-syck=internal \
		--without-readline \
		--without-augeas \
		${WITH_CRYPTO} \
		--without-libtasn1 \
		--without-pakchois \
		--without-gnutls \
		${WITH_NEON} \
		--with-pcre \
		--enable-utf8 \
		--without-uuid \
		--with-attr \
		--with-acl \
		--without-xar \
		--with-popt=external \
		${WITH_KEYUTILS} \
		--with-pthreads \
		${WITH_LIBELF} \
		--without-cudf \
		--without-ficl \
		--without-aterm \
		--without-nix \
		--without-bash \
		--without-rc \
		--without-js \
		--without-gpsee \
		${WITH_PYTHON} \
		${WITH_PERL} \
		--without-ruby \
		--without-squirrel \
		--with-build-extlibdep \
		--with-build-maxextlibdep \
		--without-valgrind \
		--disable-openmp \
		--enable-build-pic \
		--enable-build-versionscript \
		--enable-build-warnings \
		--enable-build-debug \
		--enable-maintainer-mode \
		--with-path-macros=${rpm_macros} \
		--with-path-lib=${libdir}/rpm \
		--with-bugreport=http://bugzilla.yoctoproject.org \
		--program-prefix="

CFLAGS_append = " -DRPM_VENDOR_WINDRIVER -DRPM_VENDOR_POKY"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-libs ${PN}-dev ${PN}-staticdev ${PN}-common ${PN}-build python-rpm-dbg python-rpm perl-module-rpm perl-module-rpm-dev ${PN}-locale"

SOLIBS = "5.4.so"

# Based on %files section in the rpm.spec

FILES_${PN} = "${bindir}/rpm \
		${bindir}/rpmconstant \
		${libdir}/rpm/rpm.* \
		${libdir}/rpm/tgpg \
		${libdir}/rpm/macros \
		${libdir}/rpm/rpmpopt \
		${libdir}/rpm/rpmdb_loadcvt \
		${libdir}/rpm/rpm2cpio \
		${libdir}/rpm/vcheck \
		${libdir}/rpm/helpers \
		${libdir}/rpm/qf \
		${libdir}/rpm/cpuinfo.yaml \
		${libdir}/rpm/bin/mtree \
		${libdir}/rpm/bin/rpmkey \
		${libdir}/rpm/bin/rpmrepo \
		${libdir}/rpm/bin/rpmspecdump \
		${libdir}/rpm/bin/wget \
		"

#		${libdir}/rpm/magic
#		${libdir}/rpm/magic.mgc
#		${libdir}/rpm/magic.mime
#		${libdir}/rpm/magic.mime.mgc
#		${libdir}/rpm/bin/db_*
#		${libdir}/rpm/bin/grep

FILES_${PN}-dbg += "${libdir}/rpm/.debug \
		${libdir}/rpm/bin/.debug \
		"

FILES_${PN}-common = "${bindir}/rpm2cpio \
		${bindir}/gendiff \
		/etc/rpm \
		/var/lib/rpm \
		/var/spool/repackage \
		"

FILES_${PN}-libs = "${libdir}/librpm-*.so \
		${libdir}/librpmconstant-*.so \
		${libdir}/librpmdb-*.so \
		${libdir}/librpmio-*.so \
		${libdir}/librpmmisc-*.so \
		${libdir}/librpmbuild-*.so \
		"

###%{_rpmhome}/lib/libxar.so.*
###%{_rpmhome}/lib/libjs.so.*
###%{_rpmhome}/lib/librpmjsm.so.*
###%{_rpmhome}/lib/rpmjsm.so


FILES_${PN}-build = "${prefix}/src/rpm \
		${bindir}/rpmbuild \
		${libdir}/rpm/brp-* \
		${libdir}/rpm/check-files \
		${libdir}/rpm/cross-build \
		${libdir}/rpm/find-debuginfo.sh \
		${libdir}/rpm/find-lang.sh \
		${libdir}/rpm/find-prov.pl \
		${libdir}/rpm/find-provides.perl \
		${libdir}/rpm/find-req.pl \
		${libdir}/rpm/find-requires.perl \
		${libdir}/rpm/getpo.sh \
		${libdir}/rpm/http.req \
		${libdir}/rpm/javadeps.sh \
		${libdir}/rpm/mono-find-provides \
		${libdir}/rpm/mono-find-requires \
		${libdir}/rpm/executabledeps.sh \
		${libdir}/rpm/libtooldeps.sh \
		${libdir}/rpm/osgideps.pl \
		${libdir}/rpm/perldeps.pl \
		${libdir}/rpm/perl.prov \
		${libdir}/rpm/perl.req \
		${libdir}/rpm/php.prov \
		${libdir}/rpm/php.req \
		${libdir}/rpm/pkgconfigdeps.sh \
		${libdir}/rpm/pythondeps.sh \
		${libdir}/rpm/bin/debugedit \
		${libdir}/rpm/bin/rpmcache \
		${libdir}/rpm/bin/rpmcmp \
		${libdir}/rpm/bin/rpmdeps \
		${libdir}/rpm/bin/rpmdigest \
		${libdir}/rpm/bin/abi-compliance-checker.pl \
		${libdir}/rpm/bin/api-sanity-autotest.pl \
		${libdir}/rpm/bin/chroot \
		${libdir}/rpm/bin/cp \
		${libdir}/rpm/bin/dbsql \
		${libdir}/rpm/bin/find \
		${libdir}/rpm/bin/install-sh \
		${libdir}/rpm/bin/lua \
		${libdir}/rpm/bin/luac \
		${libdir}/rpm/bin/mkinstalldirs \
		${libdir}/rpm/bin/rpmlua \
		${libdir}/rpm/bin/rpmluac \
		${libdir}/rpm/bin/sqlite3 \
		${libdir}/rpm/lib/liblua.a \
		${libdir}/rpm/lib/liblua.la \
		${libdir}/rpm/macros.d/cmake \
		${libdir}/rpm/macros.d/java \
		${libdir}/rpm/macros.d/libtool \
		${libdir}/rpm/macros.d/mandriva \
		${libdir}/rpm/macros.d/mono \
		${libdir}/rpm/macros.d/perl \
		${libdir}/rpm/macros.d/php \
		${libdir}/rpm/macros.d/pkgconfig \
		${libdir}/rpm/macros.d/python \
		${libdir}/rpm/macros.d/ruby \
		${libdir}/rpm/macros.d/selinux \
		${libdir}/rpm/macros.d/tcl \
		${libdir}/rpm/macros.rpmbuild \
		${libdir}/rpm/u_pkg.sh \
		${libdir}/rpm/vpkg-provides.sh \
		${libdir}/rpm/vpkg-provides2.sh \
		${libdir}/rpm/perfile_rpmdeps.sh \
		"
RDEPENDS_${PN}-build = "file"

#%rpmattr       %{_rpmhome}/gem_helper.rb
#%rpmattr       %{_rpmhome}/symclash.*

FILES_python-rpm-dbg = "${libdir}/python*/rpm/.debug/_*"
FILES_python-rpm = "${libdir}/python*/rpm"

FILES_perl-module-rpm = "${libdir}/perl/*/* \
		"

FILES_perl-module-rpm-dev = "${prefix}/share/man/man3/RPM* \
		"

FILE_${PN}-dev = "${includedir}/rpm \
		${libdir}/librpm.la \
		${libdir}/librpm.so \
		${libdir}/librpmconstant.la \
		${libdir}/librpmconstant.so \
		${libdir}/librpmdb.la \
		${libdir}/librpmdb.so \
		${libdir}/librpmio.la \
		${libdir}/librpmio.so \
		${libdir}/librpmmisc.la \
		${libdir}/librpmmisc.so \
		${libdir}/librpmbuild.la \
		${libdir}/librpmbuild.so \
		${libdir}/pkgconfig/rpm.pc \
		"

FILE_${PN}-staticdev = " \
		${libdir}/librpm.a \
		${libdir}/librpmconstant.a \
		${libdir}/librpmdb.a \
		${libdir}/librpmio.a \
		${libdir}/librpmmisc.a \
		${libdir}/librpmbuild.a \
		"

###%{_rpmhome}/lib/libxar.a
###%{_rpmhome}/lib/libxar.la
###%{_rpmhome}/lib/libxar.so
###%{_rpmhome}/lib/libjs.a
###%{_rpmhome}/lib/libjs.la
###%{_rpmhome}/lib/libjs.so
###%{_rpmhome}/lib/librpmjsm.a
###%{_rpmhome}/lib/librpmjsm.la
###%{_rpmhome}/lib/librpmjsm.so

do_configure() {
	# Disable tests!
	echo "all:" > tests/Makefile.am

	./autogen.sh

	oe_runconf
}

do_install_append() {
	sed -i -e 's,%__check_files,#%%__check_files,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__scriptlet_requires,#%%__scriptlet_requires,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__perl_provides,#%%__perl_provides,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__perl_requires,#%%__perl_requires,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%_repackage_all_erasures[^_].*,%_repackage_all_erasures 0,' ${D}/${libdir}/rpm/macros

	# Enable Debian style arbitrary tags...
	sed -i -e 's,%_arbitrary_tags[^_].*,%_arbitrary_tags %{_arbitrary_tags_debian},' ${D}/${libdir}/rpm/macros

	install -m 0755 ${WORKDIR}/pythondeps.sh ${D}/${libdir}/rpm/pythondeps.sh
	install -m 0755 ${WORKDIR}/perfile_rpmdeps.sh ${D}/${libdir}/rpm/perfile_rpmdeps.sh

	# Remove unpackaged files (based on list in rpm.spec)
	rm -f ${D}/${libdir}/rpm/{Specfile.pm,cpanflute,cpanflute2,rpmdiff,rpmdiff.cgi,sql.prov,sql.req,tcl.req,trpm}

	rm -f ${D}/${mandir}/man8/rpmcache.8*
	rm -f ${D}/${mandir}/man8/rpmgraph.8*
	rm -f ${D}/${mandir}/*/man8/rpmcache.8*
	rm -f ${D}/${mandir}/*/man8/rpmgraph.8*
	rm -rf ${D}/${mandir}/{fr,ko}

	rm -f ${D}/${includedir}/popt.h
	rm -f ${D}/${libdir}/libpopt.*
	rm -f ${D}/${libdir}/pkgconfig/popt.pc
	rm -f ${D}/${datadir}/locale/*/LC_MESSAGES/popt.mo
	rm -f ${D}/${mandir}/man3/popt.3

	rm -f ${D}/${mandir}/man1/xar.1*
	rm -f ${D}/${bindir}/xar
	rm -rf ${D}/${includedir}/xar
	rm -f ${D}/${libdir}/libxar*

	rm -f ${D}/${bindir}/lz*
	rm -f ${D}/${bindir}/unlzma
	rm -f ${D}/${bindir}/unxz
	rm -f ${D}/${bindir}/xz*
	rm -rf ${D}/${includedir}/lzma*
	rm -f ${D}/${mandir}/man1/lz*.1
	rm -f ${D}/${libdir}/pkgconfig/liblzma*

	rm -f ${D}/${libdir}/python%{with_python_version}/site-packages/*.{a,la}
	rm -f ${D}/${libdir}/python%{with_python_version}/site-packages/rpm/*.{a,la}

	#find ${D}/${libdir}/perl5 -type f -a \( -name perllocal.pod -o -name .packlist \
	#	-o \( -name '*.bs' -a -empty \) \) -exec rm -f {} ';'
	#find ${D}/${libdir}/perl5 -type d -depth -exec rmdir {} 2>/dev/null ';'

	# We don't want the default macro set
	rm -rf ${D}/${libdir}/rpm/{i[3456]86*,athlon*,pentium*,x86_64*,alpha*,sparc*,ia64*,ppc*,s390*,armv[34][lb]*,armv[345]*,mips*,noarch*}

	rm -f ${D}/${libdir}/rpm/dbconvert.sh

	rm -f ${D}/${libdir}/rpm/libsqldb.*
}

do_install_append_virtclass-native() {
        create_wrapper ${D}/${bindir}/rpm \
		RPM_USRLIBRPM=${STAGING_LIBDIR_NATIVE}/rpm \
		RPM_ETCRPM=${STAGING_ETCDIR_NATIVE}/rpm \
		RPM_LOCALEDIRRPM=${STAGING_DATADIR_NATIVE}/locale

        create_wrapper ${D}/${bindir}/rpm2cpio \
		RPM_USRLIBRPM=${STAGING_LIBDIR_NATIVE}/rpm \
		RPM_ETCRPM=${STAGING_ETCDIR_NATIVE}/rpm \
		RPM_LOCALEDIRRPM=${STAGING_DATADIR_NATIVE}/locale

        create_wrapper ${D}/${bindir}/rpmbuild \
		RPM_USRLIBRPM=${STAGING_LIBDIR_NATIVE}/rpm \
		RPM_ETCRPM=${STAGING_ETCDIR_NATIVE}/rpm \
		RPM_LOCALEDIRRPM=${STAGING_DATADIR_NATIVE}/locale

        create_wrapper ${D}/${bindir}/rpmconstant \
		RPM_USRLIBRPM=${STAGING_LIBDIR_NATIVE}/rpm \
		RPM_ETCRPM=${STAGING_ETCDIR_NATIVE}/rpm \
		RPM_LOCALEDIRRPM=${STAGING_DATADIR_NATIVE}/locale

	for rpm_binary in ${D}/${libdir}/rpm/bin/rpm*; do
        	create_wrapper $rpm_binary
			RPM_USRLIBRPM=${STAGING_LIBDIR_NATIVE}/rpm \
			RPM_ETCRPM=${STAGING_ETCDIR_NATIVE}/rpm \
			RPM_LOCALEDIRRPM=${STAGING_DATADIR_NATIVE}/locale
	done

	# Adjust popt macros to match...
	cat ${D}/${libdir}/rpm/rpmpopt | sed -e "s,^\(rpm[^ 	]*\)\([ 	]\),\1.real\2," > ${D}/${libdir}/rpm/rpmpopt.new
	mv ${D}/${libdir}/rpm/rpmpopt.new ${D}/${libdir}/rpm/rpmpopt
}

BBCLASSEXTEND = "native"
