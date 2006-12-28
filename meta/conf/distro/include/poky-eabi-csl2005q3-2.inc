#
# Poky configuration to use EABI
#

PREFERRED_PROVIDER_virtual/arm-poky-linux-gnueabi-libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/arm-linux-libc-for-gcc = "glibc-intermediate"

PREFERRED_VERSION_gcc ?= "3.4.4+csl-arm-2005q3-2"
PREFERRED_VERSION_gcc-cross ?= "3.4.4+csl-arm-2005q3-2"
PREFERRED_VERSION_gcc-cross-initial ?= "3.4.4+csl-arm-2005q3-2"
PREFERRED_VERSION_binutils ?= "2.16+csl-arm-2005q3-2"
PREFERRED_VERSION_binutils-cross ?= "2.16+csl-arm-2005q3-2"
PREFERRED_VERSION_glibc ?= "2.3.6+csl-arm-2005q3-2"
PREFERRED_VERSION_glibc-intermediate ?= "2.3.6+csl-arm-2005q3-2"
PREFERRED_VERSION_elfutils ?= "0.108"

PREFERRED_VERSION_linux-libc-headers ?= "2.6.12rc3+csl-arm-2005q3-2"

#Use the ARM EABI when building for an ARM cpu. We can't use overrides
#here because this breaks all places where ":=" is used.
TARGET_OS = "linux${@['','-gnueabi'][bb.data.getVar('TARGET_ARCH',d,1)=='arm']}"
