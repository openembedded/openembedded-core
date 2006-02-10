inherit base

# Native packages are built indirectly via dependency,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

PACKAGES = ""
PACKAGE_ARCH = "${BUILD_ARCH}"

# When this class has packaging enabled, setting 
# RPROVIDES becomes unnecessary.
RPROVIDES = "${PN}"

# Need to resolve package RDEPENDS as well as DEPENDS
BUILD_ALL_DEPS = "1"

# Break the circular dependency as a result of DEPENDS
# in package.bbclass
PACKAGE_DEPENDS = ""

TARGET_ARCH = "${BUILD_ARCH}"
TARGET_OS = "${BUILD_OS}"
TARGET_VENDOR = "${BUILD_VENDOR}"
TARGET_PREFIX = "${BUILD_PREFIX}"
TARGET_CC_ARCH = "${BUILD_CC_ARCH}"

HOST_ARCH = "${BUILD_ARCH}"
HOST_OS = "${BUILD_OS}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"

CPPFLAGS = "${BUILD_CPPFLAGS}"
CFLAGS = "${BUILD_CFLAGS}"
CXXFLAGS = "${BUILD_CFLAGS}"
LDFLAGS = "${BUILD_LDFLAGS}"

# Path prefixes
base_prefix = "${exec_prefix}"
prefix = "${STAGING_DIR}"
exec_prefix = "${STAGING_DIR}/${BUILD_ARCH}-${BUILD_OS}"

# Base paths
base_bindir = "${base_prefix}/bin"
base_sbindir = "${base_prefix}/bin"
base_libdir = "${base_prefix}/lib"

# Architecture independent paths
sysconfdir = "${prefix}/etc"
sharedstatedir = "${prefix}/com"
localstatedir = "${prefix}/var"
infodir = "${datadir}/info"
mandir = "${datadir}/man"
docdir = "${datadir}/doc"
servicedir = "${prefix}/srv"

# Architecture dependent paths
bindir = "${exec_prefix}/bin"
sbindir = "${exec_prefix}/bin"
libexecdir = "${exec_prefix}/libexec"
libdir = "${exec_prefix}/lib"
includedir = "${exec_prefix}/include"
oldincludedir = "${exec_prefix}/include"

# Datadir is made arch dependent here, primarily
# for autoconf macros, and other things that
# may be manipulated to handle crosscompilation
# issues.
datadir = "${exec_prefix}/share"

do_stage () {
	if [ "${INHIBIT_NATIVE_STAGE_INSTALL}" != "1" ]
	then
		oe_runmake install
	fi
}

do_install () {
	true
}
