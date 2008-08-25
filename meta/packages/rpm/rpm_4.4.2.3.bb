DESCRIPTION = "The RPM Package Manager."
HOMEPAGE = "http://rpm.org/"
LICENSE = "LGPL GPL"
DEPENDS = "zlib beecrypt file popt python"
PR = "r4"

SRC_URI = "http://www.rpm.org/releases/rpm-4.4.x/rpm-4.4.2.3.tar.gz \
           file://external-tools.patch;patch=1 \
	   file://cross_libpaths.patch;patch=1"

inherit autotools gettext

S = "${WORKDIR}/rpm-${PV}"

acpaths = "-I ${S}/db/dist/aclocal -I ${S}/db/dist/aclocal_java"

EXTRA_OECONF = "--with-python=${PYTHONVER} \
		--with-python-incdir=${STAGING_INCDIR}/python${PYTHONVER} \
		--with-python-libdir=${STAGING_LIBDIR}/python${PYTHONVER} \
		--without-apidocs \
		--without-selinux \
		--without-lua \
		--without-dmalloc \
		--without-efence"

# Handle the db MUTEX settings here, the POSIX library is
# the default - "POSIX/pthreads/library".
# Don't ignore the nice SWP instruction on the ARM:
# These enable the ARM assembler mutex code, this won't
# work with thumb compilation...
ARM_MUTEX = "--with-mutex=ARM/gcc-assembly"
MUTEX = ""
MUTEX_arm = "${ARM_MUTEX}"
MUTEX_armeb = "${ARM_MUTEX}"
EXTRA_OECONF += "${MUTEX}"

do_configure () {
	rm ${S}/popt/ -Rf
	rm ${S}/db/dist/configure.in -f
	cd ${S}/db/dist/aclocal
	rm libtool* -f
	for i in `ls *.ac`; do
	    j=`echo $i | sed 's/.ac/.m4/g'`
	    mv $i $j
	done
	cd ${S}/db/dist/aclocal_java
	for i in `ls *.ac`; do
	    j=`echo $i | sed 's/.ac/.m4/g'`
	    mv $i $j
	done
	cd ${S}
	autotools_do_configure
	cd ${S}/db/dist
	. ./RELEASE
	# Edit version information we couldn't pre-compute.
	(echo "1,\$s/__EDIT_DB_VERSION_MAJOR__/$DB_VERSION_MAJOR/g" &&
	 echo "1,\$s/__EDIT_DB_VERSION_MINOR__/$DB_VERSION_MINOR/g" &&
	 echo "1,\$s/__EDIT_DB_VERSION_PATCH__/$DB_VERSION_PATCH/g" &&
	 echo "1,\$s/__EDIT_DB_VERSION_STRING__/$DB_VERSION_STRING/g" &&
	 echo "1,\$s/__EDIT_DB_VERSION_UNIQUE_NAME__/$DB_VERSION_UNIQUE_NAME/g" &&
	 echo "1,\$s/__EDIT_DB_VERSION__/$DB_VERSION/g" &&
	 echo "w" &&
	 echo "q") | ed configure
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
		    ${EXTRA_OECONF} \
		    --with-pic

}

def rpm_python_version(d):
	import os, bb
	staging_incdir = bb.data.getVar( "STAGING_INCDIR", d, 1 )
	if os.path.exists( "%s/python2.5" % staging_incdir ): return "2.5"
	if os.path.exists( "%s/python2.4" % staging_incdir ): return "2.4"
	if os.path.exists( "%s/python2.3" % staging_incdir ): return "2.3"
	raise "No Python in STAGING_INCDIR. Forgot to build python/python-native?"

PYTHONVER = "${@rpm_python_version(d)}"
