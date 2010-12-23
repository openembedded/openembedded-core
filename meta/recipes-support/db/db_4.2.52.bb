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
HOMEPAGE = "http://www.oracle.com/technology/products/berkeley-db/db/index.html"
LICENSE = "BSD Sleepycat"
VIRTUAL_NAME = "virtual/db"
VIRTUAL_NAME_virtclass-native = "virtual/db-native"

LIC_FILES_CHKSUM = "file://../LICENSE;md5=6b31228067ad1236eceaaaf187ad6d1e"

CONFLICTS = "db3"
CONFLICTS_virtclass-native = "db3-native"
PR = "r8"

SRC_URI = "http://download.oracle.com/berkeley-db/db-${PV}.tar.gz \
           file://configure_fixes.patch;patch=1 "

SRC_URI[md5sum] = "8b5cff6eb83972afdd8e0b821703c33c"
SRC_URI[sha256sum] = "f4bddd8d1b4cde0daf5e13e3493ed62a25b736b0bf258e1d929e47bc6a82a28c"
#SRC_URI_MD5 = "http://downloads.sleepycat.com/db-${PV}.tar.gz.md5"
#TODO SRC_URI += "file://arm-thumb-mutex.patch;patch=1"

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
FILES_${PN} = "${libdir}/libdb-4*so*"
FILES_${PN}-bin = "${bindir}/*"
# The dev package has the .so link (as in db3) and the .a's -
# it is therefore incompatible (cannot be installed at the
# same time) as the db3 package
FILES_${PN}-dev = "${includedir} ${libdir}/*"

#configuration - set in local.conf to override
DB4_CONFIG ?= " --disable-cryptography --disable-queue --disable-replication --disable-verify --enable-hash"
EXTRA_OECONF = "${DB4_CONFIG}"

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

ARM_INSTRUCTION_SET = "arm"

# Cancel the site stuff - it's set for db3 and destroys the
# configure.
CONFIG_SITE = ""
do_configure() {
	rm -f ${S}/config.sub
	cp ${STAGING_DATADIR_NATIVE}/gnu-config/config.sub ${S}/config.sub 
	oe_runconf
}

do_install_append() {
	# The docs end up in /usr/docs - not right.
	if test -d "${D}/${prefix}/docs"
	then
		mkdir -p "${D}/${datadir}"
		test ! -d "${D}/${docdir}" || rm -rf "${D}/${docdir}"
		mv "${D}/${prefix}/docs" "${D}/${docdir}"
	fi
}

# The db package contains symlinks that trip up insane
INSANE_SKIP_db = "1"

BBCLASSEXTEND = "native nativesdk"
