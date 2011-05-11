#
# Configuration to use external CSL 2008q3-72 toolchain (ARM EABI)
#

TARGET_VENDOR = "-none"

EXTERNAL_TOOLCHAIN = "/usr/local/csl/arm-2008q3"

PATH =. ":${EXTERNAL_TOOLCHAIN}/bin:"

PREFERRED_PROVIDER_linux-libc-headers = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/arm-none-linux-gnueabi-gcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/arm-none-linux-gnueabi-gcc-initial = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/arm-none-linux-gnueabi-gcc-intermediate = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/arm-none-linux-gnueabi-binutils = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/arm-none-linux-gnueabi-libc-for-gcc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libc = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libintl = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/libiconv = "external-csl-toolchain"
PREFERRED_PROVIDER_glibc-thread-db = "external-csl-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "external-csl-toolchain"

PREFERRED_VERSION_external-csl-toolchain = "2008q3-72"

TARGET_CPPFLAGS_prepend = " -isystem${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/include "
TARGET_LDFLAGS_prepend = " -L${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib -Wl,-rpath-link,${EXTERNAL_TOOLCHAIN}/${TARGET_SYS}/lib "

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"
