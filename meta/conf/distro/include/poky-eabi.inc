#
# Poky's default configuration (ARM EABI)
# gcc 4.2.3, binutils 2.18, glibc 2.5
# use gcc 4.3.1 for armv7 targets
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "glibc"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "gcc-cross-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

GCCVERSION ?= "4.3.3"
# 4.2.3+csl-arm-2008q1-126 doesn't work

BINUVERSION = "2.18"

PREFERRED_VERSION_gcc ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-initial ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-intermediate ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-sdk ?= "${GCCVERSION}"
PREFERRED_VERSION_binutils ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-cross ?= "${BINUVERSION}"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.29"
PREFERRED_VERSION_glibc ?= "2.6.1"
PREFERRED_VERSION_glibc_mips ?= "2.9"
PREFERRED_VERSION_glibc-initial ?= "2.6.1"
PREFERRED_VERSION_glibc-initial_mips ?= "2.9"
PREFERRED_VERSION_elfutils ?= "0.131"

# Temporary preferred versions for PPC
POWERPC_GCCVERSION ?= "4.2.3"
POWERPC_BINUVERSION = "2.19"
POWERPC_GLIBCVERSION ?= "2.6.1"
POWERPC_LINUXLIBCVERSION ?= "2.6.20"

PREFERRED_VERSION_gcc_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-cross_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-cross-initial_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-cross-intermediate_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk-initial_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk-intermediate_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_gcc-cross-canadian_powerpc ?= "${POWERPC_GCCVERSION}"
PREFERRED_VERSION_binutils_powerpc ?= "${POWERPC_BINUVERSION}"
PREFERRED_VERSION_binutils-cross_powerpc ?= "${POWERPC_BINUVERSION}"
PREFERRED_VERSION_binutils-crosssdk_powerpc ?= "${POWERPC_BINUVERSION}"
PREFERRED_VERSION_binutils-cross-canadian_powerpc ?= "${POWERPC_BINUVERSION}"
PREFERRED_VERSION_linux-libc-headers_powerpc ?= "${POWERPC_LINUXLIBCVERSION}"
PREFERRED_VERSION_linux-libc-headers-nativesdk_powerpc ?= "${POWERPC_LINUXLIBCVERSION}"
PREFERRED_VERSION_glibc_powerpc ?= "${POWERPC_GLIBCVERSION}"
PREFERRED_VERSION_glibc-initial_powerpc ?= "${POWERPC_GLIBCVERSION}"
PREFERRED_VERSION_glibc-nativesdk_powerpc ?= "${POWERPC_GLIBCVERSION}"
PREFERRED_VERSION_glibc-initial-nativesdk_powerpc ?= "${POWERPC_GLIBCVERSION}"
PREFERRED_VERSION_elfutils_powerpc ?= "0.131"
PREFERRED_VERSION_u-boot-mkimage-native_powerpc ?= "2009.08"
PREFERRED_VERSION_udev_powerpc ?= "124"

require conf/distro/include/as-needed.inc


