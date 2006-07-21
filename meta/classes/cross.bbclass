# Cross packages are built indirectly via dependency,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

PACKAGES = ""

HOST_ARCH = "${BUILD_ARCH}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_OS = "${BUILD_OS}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"

CPPFLAGS = "${BUILD_CPPFLAGS}"
CFLAGS = "${BUILD_CFLAGS}"
CXXFLAGS = "${BUILD_CFLAGS}"
LDFLAGS = "${BUILD_LDFLAGS}"
LDFLAGS_build-darwin = "-L${STAGING_DIR}/${BUILD_SYS}/lib "

# Overrides for paths

# Path prefixes
base_prefix = "${exec_prefix}"
prefix = "${CROSS_DIR}"
exec_prefix = "${prefix}"

# Base paths
base_bindir = "${base_prefix}/bin"
base_sbindir = "${base_prefix}/bin"
base_libdir = "${base_prefix}/lib"

# Architecture independent paths
datadir = "${prefix}/share"
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

do_stage () {
	oe_runmake install
}

do_install () {
	:
}
