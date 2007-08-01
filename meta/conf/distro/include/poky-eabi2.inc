#
# Poky configuration to use EABI
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/arm-poky-linux-gnueabi-libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/arm-linux-libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

PREFERRED_VERSION_gcc ?= "4.1.1"
PREFERRED_VERSION_gcc-cross ?= "4.1.1"
PREFERRED_VERSION_gcc-cross-initial ?= "4.1.1"
PREFERRED_VERSION_binutils ?= "2.16.91.0.7"
PREFERRED_VERSION_binutils-cross ?= "2.16.91.0.7"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.15.99"
PREFERRED_VERSION_glibc ?= "2.4"
PREFERRED_VERSION_glibc-intermediate ?= "2.4"
PREFERRED_VERSION_elfutils ?= "0.108"

GLIBCTARGETOS = "linux${@['','-gnueabi'][bb.data.getVar('TARGET_ARCH',d,1) in ['arm', 'armeb']]}"
UCLIBCTARGETOS = "linux${@['-uclibc','-uclibcgnueabi'][bb.data.getVar('TARGET_ARCH',d,1) in ['arm', 'armeb']]}"
