#
# Poky configuration to use CSL 2006q1-6 derived toolchain (ARM EABI)
# gcc 4.1.0-csl, binutils 2.17-csl, glibc 2.4
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

PREFERRED_VERSION_gcc ?= "4.1.0+csl-arm-2006q1-6"
PREFERRED_VERSION_gcc-cross ?= "4.1.0+csl-arm-2006q1-6"
PREFERRED_VERSION_gcc-cross-initial ?= "4.1.0+csl-arm-2006q1-6"
PREFERRED_VERSION_binutils ?= "2.17+csl-arm-2006q1-6"
PREFERRED_VERSION_binutils-cross ?= "2.17+csl-arm-2006q1-6"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.15.99"
PREFERRED_VERSION_glibc ?= "2.4"
PREFERRED_VERSION_glibc-intermediate ?= "2.4"
PREFERRED_VERSION_elfutils ?= "0.108"
