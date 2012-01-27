#
# Configuration to use external CSL toolchain
#

TARGET_VENDOR = "-none"

EXTERNAL_TOOLCHAIN ?= "/usr/local/csl/${TARGET_ARCH}"

PATH =. "${EXTERNAL_TOOLCHAIN}/bin:"

PREFERRED_PROVIDER_linux-libc-headers = "external-csl-toolchain"
PREFERRED_PROVIDER_linux-libc-headers-dev = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}compilerlibs = "external-csl-toolchain"
PREFERRED_PROVIDER_libgcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libintl = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libiconv = "external-csl-toolchain"
PREFERRED_PROVIDER_glibc-thread-db = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "external-csl-toolchain"

TARGET_CPPFLAGS_prepend = " -isystem${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/include "
TARGET_LDFLAGS_prepend = " -L${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib -Wl,-rpath-link,${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib "

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"
