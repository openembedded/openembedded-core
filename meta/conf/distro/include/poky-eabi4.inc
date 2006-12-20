#
# Poky configuration to use EABI
#

PREFERRED_PROVIDER_virtual/arm-poky-linux-gnueabi-libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/arm-linux-libc-for-gcc = "glibc-intermediate"

PREFERRED_VERSION_gcc ?= "4.1.1"
PREFERRED_VERSION_gcc-cross ?= "4.1.1"
PREFERRED_VERSION_gcc-cross-initial ?= "4.1.1"
PREFERRED_VERSION_binutils ?= "2.17.50.0.5"
PREFERRED_VERSION_binutils-cross ?= "2.17.50.0.5"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.15.99"
PREFERRED_VERSION_glibc ?= "2.5"
PREFERRED_VERSION_glibc-intermediate ?= "2.5"
PREFERRED_VERSION_glibc-initial ?= "2.5"
PREFERRED_VERSION_elfutils ?= "0.108"

#Use the ARM EABI when building for an ARM cpu. We can't use overrides
#here because this breaks all places where ":=" is used.
TARGET_VENDOR = "${@['','-poky'][bb.data.getVar('TARGET_ARCH',d,1)=='arm']}"
TARGET_OS = "linux${@['','-gnueabi'][bb.data.getVar('TARGET_ARCH',d,1)=='arm']}"