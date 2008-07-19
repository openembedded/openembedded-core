# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"

# Save MULTIMACH_ARCH
OLD_MULTIMACH_ARCH := "${MULTIMACH_ARCH}"
# Save PACKAGE_ARCH
OLD_PACKAGE_ARCH := ${PACKAGE_ARCH}
PACKAGE_ARCH = "${BUILD_ARCH}-${OLD_PACKAGE_ARCH}-sdk"
# Also save BASE_PACKAGE_ARCH since HOST_ARCH can influence it
OLD_BASE_PACKAGE_ARCH := "${BASE_PACKAGE_ARCH}"
BASE_PACKAGE_ARCH = "${OLD_BASE_PACKAGE_ARCH}"

STAGING_DIR_HOST = "${STAGING_DIR}/${HOST_SYS}-sdk"
STAGING_DIR_TARGET = "${STAGING_DIR}/${OLD_MULTIMACH_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

HOST_ARCH = "${BUILD_ARCH}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_OS = "${BUILD_OS}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"
#HOST_SYS = "${HOST_ARCH}${TARGET_VENDOR}-${HOST_OS}"

CPPFLAGS = "${BUILDSDK_CPPFLAGS}"
CFLAGS = "${BUILDSDK_CFLAGS}"
CXXFLAGS = "${BUILDSDK_CFLAGS}"
LDFLAGS = "${BUILDSDK_LDFLAGS}"

# Path prefixes
prefix = "${SDK_PREFIX}"
exec_prefix = "${prefix}"
base_prefix = "${prefix}"

# Base paths
export base_bindir = "${prefix}/bin"
export base_sbindir = "${prefix}/bin"
export base_libdir = "${prefix}/lib"

# Architecture independent paths
export datadir = "${prefix}/share"
export sysconfdir = "${prefix}/etc"
export sharedstatedir = "${datadir}/com"
export localstatedir = "${prefix}/var"
export infodir = "${datadir}/info"
export mandir = "${datadir}/man"
export docdir = "${datadir}/doc"
export servicedir = "${prefix}/srv"

# Architecture dependent paths
export bindir = "${prefix}/bin"
export sbindir = "${prefix}/bin"
export libexecdir = "${prefix}/libexec"
export libdir = "${prefix}/lib"
export includedir = "${prefix}/include"
export oldincludedir = "${prefix}/include"

FILES_${PN} = "${prefix}"
FILES_${PN}-dbg += "${prefix}/.debug \
                    ${prefix}/bin/.debug \
                   "

export PKG_CONFIG_SYSROOT_DIR = "${STAGING_DIR_HOST}"
