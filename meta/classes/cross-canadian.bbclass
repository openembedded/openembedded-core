# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"

# Save MULTIMACH_ARCH
OLD_MULTIMACH_ARCH := "${MULTIMACH_ARCH}"
# Save PACKAGE_ARCH
OLD_PACKAGE_ARCH := ${PACKAGE_ARCH}
PACKAGE_ARCH = "${SDK_ARCH}-${TARGET_ARCH}-canadian"
PACKAGE_ARCHS = "${PACKAGE_ARCH}"
# Also save BASE_PACKAGE_ARCH since HOST_ARCH can influence it
OLD_BASE_PACKAGE_ARCH := "${BASE_PACKAGE_ARCH}"
BASE_PACKAGE_ARCH = "${OLD_BASE_PACKAGE_ARCH}"

INHIBIT_DEFAULT_DEPS = "1"

STAGING_DIR_HOST = "${STAGING_DIR}/${HOST_SYS}-nativesdk"
STAGING_DIR_TARGET = "${STAGING_DIR}/${BASEPKG_TARGET_SYS}"

PATH_append = ":${TMPDIR}/sysroots/${HOST_ARCH}/${bindir_cross}"
PKGDATA_DIR = "${TMPDIR}/pkgdata/${HOST_ARCH}-nativesdk${HOST_VENDOR}-${HOST_OS}"
PKGHIST_DIR = "${TMPDIR}/pkghistory/${HOST_ARCH}-nativesdk${HOST_VENDOR}-${HOST_OS}/"

HOST_ARCH = "${SDK_ARCH}"
HOST_VENDOR = "${SDK_VENDOR}"
HOST_OS = "${SDK_OS}"
HOST_PREFIX = "${SDK_PREFIX}"
HOST_CC_ARCH = "${SDK_CC_ARCH}"

CPPFLAGS = "${BUILDSDK_CPPFLAGS}"
CFLAGS = "${BUILDSDK_CFLAGS}"
CXXFLAGS = "${BUILDSDK_CFLAGS}"
LDFLAGS = "${BUILDSDK_LDFLAGS}"

DEPENDS_GETTEXT = "gettext-native gettext-nativesdk"

# Path mangling needed by the cross packaging
# Note that we use := here to ensure that libdir and includedir are
# target paths.
target_libdir := "${libdir}"
target_includedir := "${includedir}"
target_base_libdir := "${base_libdir}"
target_prefix := "${prefix}"
target_exec_prefix := "${exec_prefix}"

# Change to place files in SDKPATH
prefix = "${SDKPATH}"
exec_prefix = "${SDKPATH}"
base_prefix = "${SDKPATH}"

FILES_${PN} = "${prefix}"
FILES_${PN}-dbg += "${prefix}/.debug \
                    ${prefix}/bin/.debug \
                   "

export PKG_CONFIG_DIR = "${STAGING_DIR_HOST}${layout_libdir}/pkgconfig"
export PKG_CONFIG_SYSROOT_DIR = "${STAGING_DIR_HOST}"

# Cross-canadian packages need to pull in nativesdk so's, sorry for ugliness
SHLIBSDIR = "${STAGING_DIR}/${SDK_ARCH}-nativesdk-pokysdk-${BUILD_OS}/shlibs"
