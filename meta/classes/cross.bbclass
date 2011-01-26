inherit relocatable

# Cross packages are built indirectly via dependency,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

# Save PACKAGE_ARCH before changing HOST_ARCH
OLD_PACKAGE_ARCH := "${PACKAGE_ARCH}"
PACKAGE_ARCH = "${OLD_PACKAGE_ARCH}"
# Also save BASE_PACKAGE_ARCH since HOST_ARCH can influence it
OLD_BASE_PACKAGE_ARCH := "${BASE_PACKAGE_ARCH}"
BASE_PACKAGE_ARCH = "${OLD_BASE_PACKAGE_ARCH}"
BASEPKG_HOST_SYS = "${HOST_ARCH}${HOST_VENDOR}-${HOST_OS}"

PACKAGES = ""

HOST_ARCH = "${BUILD_ARCH}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_OS = "${BUILD_OS}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"

STAGING_DIR_HOST = "${STAGING_DIR}/${BASEPKG_HOST_SYS}"

export PKG_CONFIG_DIR = "${STAGING_DIR}/${BASE_PACKAGE_ARCH}${TARGET_VENDOR}-${TARGET_OS}${libdir}/pkgconfig"
export PKG_CONFIG_SYSROOT_DIR = "${STAGING_DIR}/${BASE_PACKAGE_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

CPPFLAGS = "${BUILD_CPPFLAGS}"
CFLAGS = "${BUILD_CFLAGS}"
CXXFLAGS = "${BUILD_CFLAGS}"
LDFLAGS = "${BUILD_LDFLAGS}"
LDFLAGS_build-darwin = "-L${STAGING_LIBDIR_NATIVE}"

TOOLCHAIN_OPTIONS = ""

DEPENDS_GETTEXT = "gettext-native"

# Path mangling needed by the cross packaging
# Note that we use := here to ensure that libdir and includedir are
# target paths.
target_libdir := "${libdir}"
target_includedir := "${includedir}"
target_base_libdir := "${base_libdir}"
target_prefix := "${prefix}"
target_exec_prefix := "${exec_prefix}"

# Overrides for paths
CROSS_TARGET_SYS_DIR = "${MULTIMACH_TARGET_SYS}"
prefix = "${STAGING_DIR_NATIVE}${prefix_native}"
base_prefix = "${STAGING_DIR_NATIVE}"
exec_prefix = "${STAGING_DIR_NATIVE}${prefix_native}"
bindir = "${exec_prefix}/bin/${CROSS_TARGET_SYS_DIR}"
sbindir = "${bindir}"
base_bindir = "${bindir}"
base_sbindir = "${bindir}"
libdir = "${exec_prefix}/lib/${CROSS_TARGET_SYS_DIR}"
libexecdir = "${exec_prefix}/libexec/${CROSS_TARGET_SYS_DIR}"

do_populate_sysroot[sstate-inputdirs] = "${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
}

