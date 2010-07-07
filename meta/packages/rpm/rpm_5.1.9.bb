DESCRIPTION = "The RPM Package Manager - relaunched"
HOMEPAGE = "http://rpm5.org/"
LICENSE = "LGPL 2.1"
DEPENDS = "zlib perl popt beecrypt python libpcre"
PR = "r0"

SRC_URI = "http://www.rpm5.org/files/rpm/rpm-5.1/${BPN}-${PV}.tar.gz \
	   file://remove-compiled-tests.patch;apply=no \
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
		--without-apidocs \
		--without-selinux \
		--without-lua \
		--without-dmalloc \
		--without-efence \
		--without-neon \
		--with-pcre=${libdir} \
		--with-path-macros=${rpm_macros}"

CFLAGS_append = " -DRPM_VENDOR_WINDRIVER"

PACKAGES += "python-rpm"
FILES_python-rpm = "${libdir}/python*/site-packages/rpm/_*"

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
