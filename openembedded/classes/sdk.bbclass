# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"

SDK_NAME = "${TARGET_ARCH}/oe"
PACKAGE_ARCH = "${BUILD_ARCH}"

HOST_ARCH = "${BUILD_ARCH}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_OS = "${BUILD_OS}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"

export CPPFLAGS = "${BUILD_CPPFLAGS}"
export CFLAGS = "${BUILD_CFLAGS}"
export CXXFLAGS = "${BUILD_CFLAGS}"
export LDFLAGS = "${BUILD_LDFLAGS}"

prefix = "/usr/local/${SDK_NAME}"
exec_prefix = "${prefix}"

FILES_${PN} = "${prefix}"
