#
# Poky's default configuration (ARM EABI)
# gcc 4.1.2, binutils 2.18, glibc 2.5
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

PREFERRED_VERSION_gcc ?= "4.1.2"
PREFERRED_VERSION_gcc-cross ?= "4.1.2"
PREFERRED_VERSION_gcc-cross-initial ?= "4.1.1"
PREFERRED_VERSION_binutils ?= "2.18"
PREFERRED_VERSION_binutils-cross ?= "2.18"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.20"
PREFERRED_VERSION_glibc ?= "2.5"
PREFERRED_VERSION_glibc-intermediate ?= "2.5"
PREFERRED_VERSION_glibc-initial ?= "2.5"
PREFERRED_VERSION_elfutils ?= "0.108"
