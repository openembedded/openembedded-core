# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"

OLD_PACKAGE_ARCH := ${PACKAGE_ARCH}
PACKAGE_ARCH = "${BUILD_ARCH}-${OLD_PACKAGE_ARCH}-sdk"

HOST_ARCH = "${BUILD_ARCH}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_OS = "${BUILD_OS}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"

CPPFLAGS = "${BUILD_CPPFLAGS}"
CFLAGS = "${BUILD_CFLAGS}"
CXXFLAGS = "${BUILD_CFLAGS}"
LDFLAGS = "${BUILD_LDFLAGS}"

prefix = "${SDK_PREFIX}"
exec_prefix = "${prefix}"
base_prefix = "${exec_prefix}"

FILES_${PN} = "${prefix}"
FILES_${PN}-dbg += "${prefix}/bin/.debug \
                    ${prefix}/sbin/.debug \
                   "
