DESCRIPTION = "The RPM Package Manager - relaunched"
DESCRIPTION_rpm-build = "The RPM Package Manager rpmbuild and related commands."
HOMEPAGE = "http://rpm5.org/"
LICENSE = "LGPL 2.1"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = "zlib perl popt beecrypt python libpcre elfutils"
PR = "r7"

SRC_URI = "http://www.rpm5.org/files/rpm/rpm-5.1/POKY/${BPN}-${PV}.tar.gz \
           file://hdraddorappend.patch \
           file://export-rpmbag-h.patch \
	   file://rpm-nrescan.patch \
	   file://rpm-autoconf.patch \
	   file://remove-compiled-tests.patch;apply=no \
	   file://perfile_rpmdeps.sh \
	   file://rpm-no-loop.patch \
	  "

SRC_URI[md5sum] = "a5deb83b451f11c04461c630937b1189"
SRC_URI[sha256sum] = "701726a6ae8283fcabc308dae523ad9599388296a562db1b3d7d7c871641af7e"
inherit autotools gettext

acpaths = "-I ${S}/db/dist/aclocal -I ${S}/db/dist/aclocal_java"

# Specify the default rpm macros in terms of adjustable variables
rpm_macros = "%{_usrlibrpm}/macros:%{_usrlibrpm}/poky/macros:%{_usrlibrpm}/poky/%{_target}/macros:~/.oerpmmacros"

EXTRA_OECONF = "--with-python=${PYTHON_BASEVERSION} \
		--with-python-inc-dir=${STAGING_INCDIR}/python${PYTHON_BASEVERSION} \
		--with-python-lib-dir=${libdir}/python${PYTHON_BASEVERSION} \
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
		--with-pcre=internal \
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
	# Manually run through the steps of the autogen.sh
	( cd pcre
	  libtoolize --quiet --copy --force --install
	  aclocal
	  autoheader
	  automake -Wall -Wno-override -a -c
	  autoconf
	)

	( cd xz
	  autopoint -f
	  rm -f \
	        codeset.m4 \
	        glibc2.m4 \
	        glibc21.m4 \
	        intdiv0.m4 \
	        intl.m4 \
	        intldir.m4 \
	        intmax.m4 \
	        inttypes-pri.m4 \
	        inttypes_h.m4 \
	        lcmessage.m4 \
	        lock.m4 \
	        longdouble.m4 \
	        longlong.m4 \
	        printf-posix.m4 \
	        size_max.m4 \
	        stdint_h.m4 \
	        uintmax_t.m4 \
	        ulonglong.m4 \
	        visibility.m4 \
	        wchar_t.m4 \
	        wint_t.m4 \
	        xsize.m4
	  libtoolize -c -f || glibtoolize -c -f
	  aclocal -I m4
	  autoconf
	  autoheader
	  automake -acf --foreign
	)

	( cd file
	  libtoolize --quiet --copy --force --install
	  aclocal
	  autoheader
	  automake -Wall -Wno-override -a -c
	  autoconf
	)

	(cd syck
	  libtoolize --quiet --copy --force --install
	  aclocal
	  autoheader
	  automake -Wall -Wno-override -a -c
	  autoconf
	)

	(cd xar
	  libtoolize --quiet --copy --force --install
	  aclocal
	  autoheader
	  automake -Wall -Wno-override -a -c
	  autoconf
	)

	rm -rf autom4te.cache || true
	libtoolize --quiet --copy --force --install
	autopoint --force
	rm -f aclocal.m4
	aclocal -I m4
	autoheader -I m4
	automake -Wall -Wno-override -a -c
	autoconf -I m4
	# end of autogen.sh steps

	export ac_cv_va_copy=C99
	oe_runconf
}

do_install_append() {
	sed -i -e 's,%__check_files,#%%__check_files,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__scriptlet_requires,#%%__scriptlet_requires,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__perl_provides,#%%__perl_provides,' ${D}/${libdir}/rpm/macros
	sed -i -e 's,%__perl_requires,#%%__perl_requires,' ${D}/${libdir}/rpm/macros

	# Enable Debian style arbitrary tags...
	sed -i -e 's,%_arbitrary_tags[^_].*,%_arbitrary_tags %{_arbitrary_tags_debian},' ${D}/${libdir}/rpm/macros

	install -m 0755 ${WORKDIR}/perfile_rpmdeps.sh ${D}/${libdir}/rpm/perfile_rpmdeps.sh
}

BBCLASSEXTEND = "native"
