# Version 4 of the Berkeley DB from Sleepycat
#
# At present this package only installs the DB code
# itself (shared libraries, .a in the dev package),
# documentation and headers.
#
# The headers have the same names as those as v3
# of the DB, only one version can be used *for dev*
# at once - DB3 and DB4 can both be installed on the
# same system at the same time if really necessary.
SECTION = "libs"
DESCRIPTION = "Berkeley DB v4."
HOMEPAGE = "http://www.sleepycat.com"
LICENSE = "BSD Sleepycat"
PR = "r1"

SRC_URI = "http://downloads.sleepycat.com/${P}.tar.gz"

inherit autotools

# At present virtual/db is only in the db4 file, but it
# should probably be in the other candidates (db3, gdbm)
# because it doesn't make any sense to have multiple
# relational databases on an embedded machine...
PROVIDES += " db4 virtual/db"

# bitbake isn't quite clever enough to deal with sleepycat,
# the distribution sits in the expected directory, but all
# the builds must occur from a sub-directory.  The following
# persuades bitbake to go to the right place
S = "${WORKDIR}/${P}/build_unix"

# The executables go in a separate package - typically there
# is no need to install these unless doing real database
# management on the system.
PACKAGES += " ${PN}-bin"

# Package contents
FILES_${PN} = "${libdir}/libdb-4*so*"
FILES_${PN}-bin = "${bindir}"
# The dev package has the .so link (as in db3) and the .a's -
# it is therefore incompatible (cannot be installed at the
# same time) as the db3 package
FILES_${PN}-dev = "${includedir} ${libdir}"

#configuration - set in local.conf to override
DB4_CONFIG ?= "--enable-o_direct --enable-smallbuild"
# Override the MUTEX setting here, the POSIX library is
# the default - "POSIX/pthreads/library".
# Don't ignore the nice SWP instruction on the ARM:
EXTRA_OECONF = "${DB4_CONFIG}"
# These enable the ARM assembler mutex code, this won't
# work with thumb compilation...
ARM_MUTEX = "--with-mutex=ARM/gcc-assembly"
ARM_MUTEX_thumb = ""
# NOTE: only tested on nslu2, should probably be _armeb
EXTRA_OECONF_nslu2 = "${DB4_CONFIG} ${ARM_MUTEX}"

# Cancel the site stuff - it's set for db3 and destroys the
# configure.
CONFIG_SITE = ""
do_configure() {
	echo '#!/bin/sh' >${S}/configure
	echo 'rm ${S}/configure' >>${S}/configure
	echo 'exec ../dist/configure "$@"' >>${S}/configure
	chmod a+x ${S}/configure
	oe_runconf
}

do_stage() {
	# The .h files get installed read-only, the autostage
	# function just uses cp -pPR, so do this by hand
	# Install, for the moment, into include/db4 to avoid
	# interfering with the db3 headers (which have the same
	# name).  -I${STAGING_INCDIR}/db4 to use db4, as opposed
	# to db3.
	rm -rf ${STAGE_TEMP}
	mkdir -p ${STAGE_TEMP}
	oe_runmake DESTDIR="${STAGE_TEMP}" install_include
	mkdir -p ${STAGING_INCDIR}/db4
	cp -pPRf ${STAGE_TEMP}/${includedir}/* ${STAGING_INCDIR}/db4
	rm -rf ${STAGE_TEMP}
	oe_libinstall -so -C .libs libdb-4.3 ${STAGING_LIBDIR}
}

do_install_append() {
	# The docs end up in /usr/docs - not right.
	if test -d "${D}/${prefix}/docs"
	then
		mkdir -p "${D}/${datadir}"
		test ! -d "${D}/${docdir}" || rmdir "${D}/${docdir}"
		mv "${D}/${prefix}/docs" "${D}/${docdir}"
	fi
}
