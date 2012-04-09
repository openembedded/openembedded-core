#
# Default toolchain configuration
#

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "gcc-cross-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}compilerlibs = "gcc-runtime"
PREFERRED_PROVIDER_gdb = "gdb"

# Default libc config
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "${TCLIBC}"
PREFERRED_PROVIDER_virtual/${SDK_PREFIX}libc-for-gcc-nativesdk ?= "${TCLIBC}-nativesdk"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-initial = "${TCLIBC}-initial"
PREFERRED_PROVIDER_virtual/${SDK_PREFIX}libc-initial-nativesdk = "${TCLIBC}-initial-nativesdk"
PREFERRED_PROVIDER_virtual/gettext ??= "gettext"

GCCVERSION ?= "4.6%"
SDKGCCVERSION ?= "4.6%"
BINUVERSION ?= "2.22"
EGLIBCVERSION ?= "2.13"
UCLIBCVERSION ?= "0.9.33"
LINUXLIBCVERSION ?= "3.2"

PREFERRED_VERSION_gcc ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-initial ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-intermediate ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk ?= "${SDKGCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk-initial ?= "${SDKGCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk-intermediate ?= "${SDKGCCVERSION}"
PREFERRED_VERSION_gcc-cross-canadian-${TRANSLATED_TARGET_ARCH} ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-runtime ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-runtime-nativesdk ?= "${SDKGCCVERSION}"
PREFERRED_VERSION_libgcc ?= "${GCCVERSION}"
PREFERRED_VERSION_libgcc-nativesdk ?= "${SDKGCCVERSION}"
PREFERRED_VERSION_binutils ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-cross ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-crosssdk ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-cross-canadian-${TRANSLATED_TARGET_ARCH} ?= "${BINUVERSION}"
PREFERRED_VERSION_linux-libc-headers ?= "${LINUXLIBCVERSION}"
PREFERRED_VERSION_linux-libc-headers-nativesdk ?= "${LINUXLIBCVERSION}"
PREFERRED_VERSION_eglibc                   ?= "${EGLIBCVERSION}"
PREFERRED_VERSION_eglibc-locale            ?= "${EGLIBCVERSION}"
PREFERRED_VERSION_eglibc-nativesdk         ?= "2.15"
PREFERRED_VERSION_eglibc-initial           ?= "${EGLIBCVERSION}"
PREFERRED_VERSION_eglibc-initial-nativesdk ?= "2.15"
PREFERRED_VERSION_cross-localedef-native   ?= "${EGLIBCVERSION}"
PREFERRED_VERSION_uclibc                   ?= "${UCLIBCVERSION}"
PREFERRED_VERSION_uclibc-initial           ?= "${UCLIBCVERSION}"
PREFERRED_VERSION_elfutils ?= "0.148"
# don't use version earlier than 1.4 for gzip-native, as it's necessary for
# some packages using an archive format incompatible with earlier gzip
PREFERRED_VERSION_gzip-native ?= "1.4"

# Setup suitable toolchain flags
require conf/distro/include/as-needed.inc

