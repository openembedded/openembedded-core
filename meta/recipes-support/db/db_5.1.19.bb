# Version 5 of the Berkeley DB from Sleepycat
#
# At present this package only installs the DB code
# itself (shared libraries, .a in the dev package),
# documentation and headers.
#
# The headers have the same names as those as v3
# of the DB, only one version can be used *for dev*
# at once - DB3 and DB5 can both be installed on the
# same system at the same time if really necessary.
SECTION = "libs"
DESCRIPTION = "Berkeley DB v5."
HOMEPAGE = "http://www.oracle.com/technology/products/berkeley-db/db/index.html"
LICENSE = "Sleepycat"
VIRTUAL_NAME ?= "virtual/db"
CONFLICTS = "db3"
PR = "r4"

SRC_URI = "http://download.oracle.com/berkeley-db/db-${PV}.tar.gz"
SRC_URI += "file://arm-thumb-mutex_db5.patch;patchdir=.."

SRC_URI[md5sum] = "76fcbfeebfcd09ba0b4d96bfdf8d884d"
SRC_URI[sha256sum] = "0194d4ca9266ba1a1c0bfbc233b18bfd05f63163453c81ebcdfdc7112d5ac850"

LIC_FILES_CHKSUM = "file://../LICENSE;md5=86f9294f39f38ef9e89690bcd2320e7a"

inherit autotools

# Put virtual/db in any appropriate provider of a
# relational database, use it as a dependency in
# place of a specific db and use:
#
# PREFERRED_PROVIDER_virtual/db
#
# to select the correct db in the build (distro) .conf
PROVIDES += "${VIRTUAL_NAME}"

# bitbake isn't quite clever enough to deal with sleepycat,
# the distribution sits in the expected directory, but all
# the builds must occur from a sub-directory.  The following
# persuades bitbake to go to the right place
S = "${WORKDIR}/db-${PV}/dist"
B = "${WORKDIR}/db-${PV}/build_unix"

# The executables go in a separate package - typically there
# is no need to install these unless doing real database
# management on the system.
PACKAGES += " ${PN}-bin"

# Package contents
FILES_${PN} = "${libdir}/libdb-5*so*"
FILES_${PN}-bin = "${bindir}/*"
# The dev package has the .so link (as in db3) and the .a's -
# it is therefore incompatible (cannot be installed at the
# same time) as the db3 package, allow the default packaging
# to sort out ${libdir}

#configuration - set in local.conf to override
# All the --disable-* options replace --enable-smallbuild, which breaks a bunch of stuff (eg. postfix)
DB5_CONFIG ?= "--enable-o_direct --disable-cryptography --disable-queue --disable-replication --disable-statistics --disable-verify --disable-compat185 --disable-sql"

EXTRA_OECONF = "${DB5_CONFIG}"

# Override the MUTEX setting here, the POSIX library is
# the default - "POSIX/pthreads/library".
# Don't ignore the nice SWP instruction on the ARM:
# These enable the ARM assembler mutex code, this won't
# work with thumb compilation...
ARM_MUTEX = "--with-mutex=ARM/gcc-assembly"
MUTEX = ""
MUTEX_arm = "${ARM_MUTEX}"
MUTEX_armeb = "${ARM_MUTEX}"
EXTRA_OECONF += "${MUTEX}"

# Cancel the site stuff - it's set for db3 and destroys the
# configure.
CONFIG_SITE = ""
do_configure() {
	export STRIP="true"
	oe_runconf
}

do_install_append() {
	mkdir -p ${D}/${includedir}/db51
	#mv ${D}/${includedir}/db_185.h ${D}/${includedir}/db51/.
	mv ${D}/${includedir}/db.h ${D}/${includedir}/db51/.
	mv ${D}/${includedir}/db_cxx.h ${D}/${includedir}/db51/.
	#mv ${D}/${includedir}/dbsql.h ${D}/${includedir}/db51/.
	#ln -s db51/db_185.h ${D}/${includedir}/db_185.h
	ln -s db51/db.h ${D}/${includedir}/db.h
	ln -s db51/db_cxx.h ${D}/${includedir}/db_cxx.h
	#ln -s db51/dbsql.h ${D}/${includedir}/dbsql.h

	# The docs end up in /usr/docs - not right.
	if test -d "${D}/${prefix}/docs"
	then
		mkdir -p "${D}/${datadir}"
		test ! -d "${D}/${docdir}" || rm -rf "${D}/${docdir}"
		mv "${D}/${prefix}/docs" "${D}/${docdir}"
	fi

	chown -R root:root ${D}
}

INSANE_SKIP_${PN} = "dev-so"

BBCLASSEXTEND = "native nativesdk"

