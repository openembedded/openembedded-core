#
# Poky configuration to use EABI
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/arm-poky-linux-gnueabi-libc-for-gcc = "glibc-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

PREFERRED_VERSION_gcc ?= "3.4.4+csl-arm-2005q3"
PREFERRED_VERSION_gcc-cross ?= "3.4.4+csl-arm-2005q3"
PREFERRED_VERSION_gcc-cross-initial ?= "3.4.4+csl-arm-2005q3"
PREFERRED_VERSION_binutils ?= "2.16.91.0.7"
PREFERRED_VERSION_binutils-cross ?= "2.16.91.0.7"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.15.99"
PREFERRED_VERSION_glibc ?= "2.5"
PREFERRED_VERSION_glibc-intermediate ?= "2.5"
PREFERRED_VERSION_glibc-initial ?= "2.5"
PREFERRED_VERSION_elfutils ?= "0.108"

GLIBCTARGETOS = "linux${@['','-gnueabi'][bb.data.getVar('TARGET_ARCH',d,1) in ['arm', 'armeb']]}"
UCLIBCTARGETOS = "linux${@['-uclibc','-uclibcgnueabi'][bb.data.getVar('TARGET_ARCH',d,1) in ['arm', 'armeb']]}"
