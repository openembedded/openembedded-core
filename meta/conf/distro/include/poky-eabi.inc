#
# Poky's default configuration
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "glibc"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "gcc-cross-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

GCCVERSION ?= "4.3.3"
BINUVERSION ?= "2.18"
GLIBCVERSION ?= "2.6.1"
LINUXLIBCVERSION ?= "2.6.29"

# Temporary preferred version overrides for PPC
GCCVERSION_powerpc ?= "4.2.3"
BINUVERSION_powerpc ?= "2.19"
GLIBCVERSION_powerpc ?= "2.6.1"
LINUXLIBCVERSION_powerpc ?= "2.6.20"
PREFERRED_VERSION_u-boot-mkimage-native_powerpc ?= "2009.08"
PREFERRED_VERSION_udev_powerpc ?= "124"

# Temporary preferred version overrides for mips
GLIBCVERSION_mips ?= "2.9"

PREFERRED_VERSION_gcc ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-initial ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-intermediate ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-sdk ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk-initial ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-crosssdk-intermediate ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-canadian ?= "${GCCVERSION}"
PREFERRED_VERSION_binutils ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-cross ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-crosssdk ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-cross-canadian ?= "${BINUVERSION}"
PREFERRED_VERSION_linux-libc-headers ?= "${LINUXLIBCVERSION}"
PREFERRED_VERSION_linux-libc-headers-nativesdk ?= "${LINUXLIBCVERSION}"
PREFERRED_VERSION_glibc ?= "${GLIBCVERSION}"
PREFERRED_VERSION_glibc-nativesdk ?= "${GLIBCVERSION}"
PREFERRED_VERSION_glibc-initial ?= "${GLIBCVERSION}"
PREFERRED_VERSION_glibc-initial-nativesdk ?= "${GLIBCVERSION}"
PREFERRED_VERSION_elfutils ?= "0.131"

require conf/distro/include/as-needed.inc


