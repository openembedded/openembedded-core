DESCRIPTION = "The RPM Package Manager - relaunched"
DESCRIPTION_rpm-build = "The RPM Package Manager rpmbuild and related commands."
HOMEPAGE = "http://rpm5.org/"
LICENSE = "LGPL 2.1"
DEPENDS = "zlib perl popt beecrypt python libpcre elfutils"
PR = "r2"

SRC_URI = "http://www.rpm5.org/files/rpm/rpm-5.1/SNAPSHOT/${BPN}-${PV}.tar.gz \
	   file://remove-compiled-tests.patch;apply=no \
	   file://perfile_rpmdeps.sh \
	  "
inherit autotools gettext

acpaths = "-I ${S}/db/dist/aclocal -I ${S}/db/dist/aclocal_java"

# Specify the default rpm macros in terms of adjustable variables
rpm_macros = "%{_usrlibrpm}/macros:%{_usrlibrpm}/poky/macros:%{_usrlibrpm}/poky/%{_target}/macros:~/.oerpmmacros"

EXTRA_OECONF = "--with-python=$PYTHONVER \
		--with-python-inc-dir=${STAGING_INCDIR}/python$PYTHONVER \
		--with-python-lib-dir=${libdir}/python$PYTHONVER \
		--with-db=internal \
		--with-db-tools-integrated \
		--with-libelf \
		--with-file=internal \
		--without-apidocs \
		--without-selinux \
		--without-lua \
		--without-dmalloc \
		--without-efence \
		--without-neon \
		--with-pcre=${libdir} \
		--with-path-macros=${rpm_macros} \
		--with-bugreport=http://bugzilla.pokylinux.org"

CFLAGS_append = " -DRPM_VENDOR_WINDRIVER"

PACKAGES =+ "rpm-build python-rpm python-rpm-dbg"

SOLIBS = "5.0.so"

FILES_rpm-build = "${bindir}/*-rpmbuild \
		${bindir}/*-gendiff \
		${bindir}/*-rpmspecdump \
		${libdir}/rpm/helpers/* \
		${libdir}/rpm/*brp* \
		${libdir}/rpm/*check-files \
		${libdir}/rpm/*cross-build \
		${libdir}/rpm/*debugedit \
		${libdir}/rpm/*dep* \
		${libdir}/rpm/*prov* \
		${libdir}/rpm/*req* \
		${libdir}/rpm/*find* \
		${libdir}/rpm/qf/* \
		"

FILES_python-rpm = "${libdir}/python*/rpm/_*"
FILES_python-rpm-dbg = "${libdir}/python*/rpm/.debug/_*"

# The mutex needs to be POSIX/pthreads/library or we can't
# share a database between host and target environments
# (there is a minor performance penalty, but not one great enough
#  to justify the pain of a more optimized approach!)
EXTRA_OECONF += "--with-mutex=POSIX/pthreads/library"

do_configure() {
	rm ${S}/db/dist/configure.in -f
	for i in `find ${S} -name *.ac`; do
		j=`echo $i | sed 's/.ac/.m4/g'`
		mv $i $j
	done
	export ac_cv_va_copy=C99
	autotools_do_configure
	cd ${S}/db3
	${S}/db3/configure \
		    --build=${BUILD_SYS} \
		    --host=${HOST_SYS} \
		    --target=${TARGET_SYS} \
		    --prefix=${prefix} \
		    --exec_prefix=${exec_prefix} \
		    --bindir=${bindir} \
		    --sbindir=${sbindir} \
		    --libexecdir=${libexecdir} \
		    --datadir=${datadir} \
		    --sysconfdir=${sysconfdir} \
		    --sharedstatedir=${sharedstatedir} \
		    --localstatedir=${localstatedir} \
		    --libdir=${libdir} \
		    --includedir=${includedir} \
		    --oldincludedir=${oldincludedir} \
		    --infodir=${infodir} \
		    --mandir=${mandir} \
		    ${EXTRA_OECONF}
}

INSTALL_ACTIONS=""

# When installing the native version, the rpm components are renamed with a
# naming transform.  We need to adjust the rpmpopt file with the same transform
INSTALL_ACTIONS_virtclass-native="sed -i -e 's,rpm,${HOST_SYS}-rpm,' ${D}/${libdir}/rpm/rpmpopt"

do_install_append() {
        ${INSTALL_ACTIONS}
	sed -i -e 's,%__check_files,#%%__check_files,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__scriptlet_requires,#%%__scriptlet_requires,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__perl_provides,#%%__perl_provides,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__perl_requires,#%%__perl_requires,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,pythondeps.sh,${HOST_SYS}-pythondeps.sh,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,phpdeps.sh,${HOST_SYS}-phpdeps.sh,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,javadeps.sh,${HOST_SYS}-javadeps.sh,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,libtooldeps.sh,${HOST_SYS}-libtooldeps.sh,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,pkgconfigdeps.sh,${HOST_SYS}-pkgconfigdeps.sh,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,executabledeps.sh,${HOST_SYS}-executabledeps.sh,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,perl.prov,${HOST_SYS}-perl.prov,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,perl.req,${HOST_SYS}-perl.req,' ${D}/${libdir}/rpm/macros

	install -m 0755 ${WORKDIR}/perfile_rpmdeps.sh ${D}/${libdir}/rpm/perfile_rpmdeps.sh

	mv ${D}/${libdir}/python$PYTHONVER/rpm/${HOST_SYS}-__init__.py \
		${D}/${libdir}/python$PYTHONVER/rpm/__init__.py

}

def rpm_python_version(d):
	import os, bb
	staging_incdir = bb.data.getVar( "STAGING_INCDIR", d, 1 )
	if os.path.exists( "%s/python2.6" % staging_incdir ): return "2.6"
	if os.path.exists( "%s/python2.5" % staging_incdir ): return "2.5"
	if os.path.exists( "%s/python2.4" % staging_incdir ): return "2.4"
	if os.path.exists( "%s/python2.3" % staging_incdir ): return "2.3"
	raise "No Python in STAGING_INCDIR. Forgot to build python/python-native?"

# Use a shell variable here since otherwise gettext trys to expand this at 
# parse time when it manipulates EXTRA_OECONF which fails
export PYTHONVER = "${@rpm_python_version(d)}"

BBCLASSEXTEND = "native"
