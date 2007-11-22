#
# Configuration to use a toolchain built by meta-toolchain.bb
#

TARGET_VENDOR = "-poky"

EXTERNAL_TOOLCHAIN = "${SDK_PREFIX}"

PATH =. ":${EXTERNAL_TOOLCHAIN}/bin:"

PREFERRED_PROVIDER_linux-libc-headers = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/arm-poky-linux-gnueabi-gcc = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/arm-poky-linux-gnueabi-gcc-initial = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/arm-poky-linux-binutils = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/arm-poky-linux-libc-for-gcc = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/i586-poky-linux-gcc = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/i586-poky-linux-gcc-initial = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/i586-poky-linux-binutils = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/i586-poky-linux-libc-for-gcc = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/libc = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/libintl = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/libiconv = "external-poky-toolchain"
PREFERRED_PROVIDER_glibc-thread-db = "external-poky-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "external-poky-toolchain"

TARGET_CPPFLAGS_prepend = " -isystem${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/include "
TARGET_LDFLAGS_prepend = " -L${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib -Wl,-rpath-link,${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib "
TARGET_CFLAGS = "${TARGET_CPPFLAGS} ${SELECTED_OPTIMIZATION}"

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"
