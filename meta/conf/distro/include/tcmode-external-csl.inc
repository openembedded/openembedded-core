#
# Configuration to use external CSL toolchain
#

EXTERNAL_TOOLCHAIN ?= "/usr/local/csl/${TARGET_ARCH}"

PATH =. "${EXTERNAL_TOOLCHAIN}/bin:"

CSL_TARGET_SYS_powerpc = "powerpc-linux-gnu"
CSL_TARGET_SYS_powerpc64 = "powerpc-linux-gnu"
CSL_TARGET_SYS_arm = "arm-none-linux-gnueabi"
CSL_TARGET_SYS_mips = "mips-linux-gnu"
CSL_TARGET_SYS_mipsel = "mips-linux-gnu"
CSL_TARGET_SYS_mips64 = "mips-linux-gnu"
CSL_TARGET_SYS = "${TARGET_SYS}"

TARGET_PREFIX = "${CSL_TARGET_SYS}-"

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

# Translate to CodeSourcery's names for their optimized files in the toolchain
def csl_target_core(d):
    coredata = {
        'armv7a-vfp-neon': 'armv7-a-neon',
        'mips': 'mips32',
        'mipsel': 'el',
        'ppce500': 'te500v1',
        'ppce500mc': 'te500mc',
        'ppce500v2': 'te500v2',
        'ppce600': 'te600'
    }
    return coredata.get(d.getVar('TUNE_PKGARCH', True), '')

CSL_TARGET_CORE = "${@csl_target_core(d)}"
