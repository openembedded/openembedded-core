SECTION = "libs"
DESCRIPTION = "Berkeley DB v3."
HOMEPAGE = "http://www.sleepycat.com"
LICENSE = "BSD Sleepycat"
PR = "r2"
VIRTUAL_NAME ?= "virtual/db"
CONFLICTS = "db"

# it doesn't make any sense to have multiple relational
# databases on an embedded machine, virtual/db allows
# a build to select the desired one.
PROVIDES += "${VIRTUAL_NAME}"

SRC_URI = "ftp://sleepycat1.inetu.net/releases/db-${PV}.tar.gz \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/db-${PV}/dist"
B = "${WORKDIR}/db-${PV}/build_unix"

inherit autotools

EXTRA_OECONF = "--enable-shared --enable-compat185 --enable-static"
EXTRA_OEMAKE = "'SHELL=/bin/sh' 'ar=`which ${AR}` cr' 'chmod=`which chmod`' \
		'cp=`which cp`' 'ln=`which ln`' 'mkdir=`which mkdir`' 'ranlib=`which ${RANLIB}`' \
		'rm=`which rm`' 'strip=`which ${STRIP}`'"

PACKAGES = "${PN} ${PN}-bin ${PN}-dev ${PN}-doc ${PN}-locale"

FILES_${PN} = "${libdir}/libdb-3.2*so*"
FILES_${PN}-bin = "${bindir}"
FILES_${PN}-dev = "${includedir} ${libdir}/libdb.so ${libdir}/libdb.a \
		   ${libdir}/libdb-3.so ${libdir}/libdb.la ${libdir}/libdb-3.2.a \
		   ${libdir}/libdb-3.2.la"

do_configure_prepend () {
	set -e
	(
		cd ${S}
		. ./RELEASE
		(echo "AC_DEFUN(AM_VERSION_SET, [" &&
		echo "AC_SUBST(DB_VERSION_MAJOR)" &&
		echo "AC_DEFINE(DB_VERSION_MAJOR, [$DB_VERSION_MAJOR])" &&
		echo "DB_VERSION_MAJOR=$DB_VERSION_MAJOR" &&
		echo "AC_SUBST(DB_VERSION_MINOR)" &&
		echo "AC_DEFINE(DB_VERSION_MINOR, [$DB_VERSION_MINOR])" &&
		echo "DB_VERSION_MINOR=$DB_VERSION_MINOR" &&
		echo "AC_SUBST(DB_VERSION_PATCH)" &&
		echo "AC_DEFINE(DB_VERSION_PATCH, [$DB_VERSION_PATCH])" &&
		echo "DB_VERSION_PATCH=$DB_VERSION_PATCH" &&
		echo "AC_SUBST(DB_VERSION_STRING)" &&
		echo "AC_DEFINE(DB_VERSION_STRING, [$DB_VERSION_STRING])" &&
		echo "DB_VERSION_STRING=\"\\\"\$DB_VERSION_STRING\\\"\"" &&
		echo "])dnl") > acinclude.m4
	)
}

do_configure () {
	rm -f ${S}/configure
	autotools_do_configure
}

do_compile () {
	oe_runmake
}

do_stage () {
	install -m 0644 db_185.h ../include/db_cxx.h db.h ${STAGING_INCDIR}/
	oe_libinstall -so -a libdb-3.2 ${STAGING_LIBDIR}
	ln -sf libdb-3.2.so ${STAGING_LIBDIR}/libdb.so
	ln -sf libdb-3.2.a ${STAGING_LIBDIR}/libdb.a
}

do_install () {
	oe_runmake \
		prefix=${D}${prefix} \
		exec_prefix=${D}${exec_prefix} \
		bindir=${D}${bindir} \
		includedir=${D}${includedir} \
		libdir=${D}${libdir} \
		docdir=${D}${docdir} \
		install
}

python do_package() {
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_${PN}', 'libdb3', d)
	bb.build.exec_func('package_do_package', d)
}
