#
# NOTE - When using this class the user is repsonsible for ensuring that
# TRANSLATED_TARGET_ARCH is added into PN. This ensures that if the TARGET_ARCH
# is changed, another nativesdk xxx-canadian-cross can be installed
#


# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"

STAGING_BINDIR_TOOLCHAIN = "${STAGING_DIR_NATIVE}${bindir_native}/${SDK_ARCH}${SDK_VENDOR}-${SDK_OS}:${STAGING_DIR_NATIVE}${bindir_native}/${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"

#
# Update BASE_PACKAGE_ARCH and PACKAGE_ARCHS
#
PACKAGE_ARCH = "${SDK_ARCH}-nativesdk"
python () {
    archs = d.getVar('PACKAGE_ARCHS', True).split()
    sdkarchs = []
    for arch in archs:
        sdkarchs.append(arch + '-nativesdk')
    d.setVar('PACKAGE_ARCHS', " ".join(sdkarchs))
}
MULTIMACH_TARGET_SYS = "${PACKAGE_ARCH}${HOST_VENDOR}-${HOST_OS}"

INHIBIT_DEFAULT_DEPS = "1"

STAGING_DIR_HOST = "${STAGING_DIR}/${HOST_ARCH}-nativesdk${HOST_VENDOR}-${HOST_OS}"

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR}/${HOST_ARCH}-nativesdk${HOST_VENDOR}-${HOST_OS}"

PATH_append = ":${TMPDIR}/sysroots/${HOST_ARCH}/${bindir_cross}"
PKGDATA_DIR = "${TMPDIR}/pkgdata/${HOST_ARCH}-nativesdk${HOST_VENDOR}-${HOST_OS}"
PKGHIST_DIR = "${TMPDIR}/pkghistory/${HOST_ARCH}-nativesdk${HOST_VENDOR}-${HOST_OS}/"

HOST_ARCH = "${SDK_ARCH}"
HOST_VENDOR = "${SDK_VENDOR}"
HOST_OS = "${SDK_OS}"
HOST_PREFIX = "${SDK_PREFIX}"
HOST_CC_ARCH = "${SDK_CC_ARCH}"
HOST_LD_ARCH = "${SDK_LD_ARCH}"
HOST_AS_ARCH = "${SDK_AS_ARCH}"

#assign DPKG_ARCH
DPKG_ARCH = "${SDK_ARCH}"

CPPFLAGS = "${BUILDSDK_CPPFLAGS}"
CFLAGS = "${BUILDSDK_CFLAGS}"
CXXFLAGS = "${BUILDSDK_CFLAGS}"
LDFLAGS = "${BUILDSDK_LDFLAGS} \
           -Wl,-rpath-link,${STAGING_LIBDIR}/.. \
           -Wl,-rpath,${libdir}/.. "

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
base_prefix = "${SDKPATHNATIVE}"
prefix = "${SDKPATHNATIVE}${prefix_nativesdk}"
exec_prefix = "${SDKPATHNATIVE}${prefix_nativesdk}"
bindir = "${exec_prefix}/bin/${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"
sbindir = "${bindir}"
base_bindir = "${bindir}"
base_sbindir = "${bindir}"
libdir = "${exec_prefix}/lib/${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"
libexecdir = "${exec_prefix}/libexec/${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}"

FILES_${PN} = "${prefix}"
FILES_${PN}-dbg += "${prefix}/.debug \
                    ${prefix}/bin/.debug \
                   "

export PKG_CONFIG_DIR = "${STAGING_DIR_HOST}${layout_libdir}/pkgconfig"
export PKG_CONFIG_SYSROOT_DIR = "${STAGING_DIR_HOST}"

# Cross-canadian packages need to pull in nativesdk dynamic libs
SHLIBSDIR = "${STAGING_DIR}/${SDK_ARCH}-nativesdk${SDK_VENDOR}-${BUILD_OS}/shlibs"

do_populate_sysroot[stamp-extra-info] = ""
do_package[stamp-extra-info] = ""
